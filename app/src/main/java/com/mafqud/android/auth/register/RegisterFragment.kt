package com.mafqud.android.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.mafqud.android.R
import com.mafqud.android.auth.openReportActivity
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBarUi
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.other.hideKeypad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private val viewModel: RegisterViewModel by viewModels()

    // variable for FirebaseAuth class
    private lateinit var mAuth: FirebaseAuth

    // string for storing our verification ID
    private var verificationId: String = ""

    //OTP remember
    private val otpState: MutableState<String> = mutableStateOf("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // @ref https://developer.android.com/jetpack/compose/interop/interop-apis#composition-strategy
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                val isDialogOpened = remember {
                    mutableStateOf(false)
                }
                val registerFormData = remember {
                    mutableStateOf(RegisterIntent.Signup())
                }
                val activeStep = remember {
                    mutableStateOf(StepCount.One)
                }
                MafQudTheme {
                    /*RegisterScreen(
                        registerFormData,
                        activeStep,
                        otpState,
                        onStepOne = { phone ->
                            otpState.value = ""
                            registerFormData.value = registerFormData.value.copy(
                                phone = phone

                            )
                            validatePhone(phone = phone)

                        },
                        onStepTwo = { otp ->
                            otpState.value = otp
                            verifyCode(otp) {
                                activeStep.value = StepCount.Three
                            }
                        },
                        onStepThree = { name, email ->
                            registerFormData.value = registerFormData.value.copy(
                                fullName = name,
                                email = email
                            )
                            validateEmail(email = email)

                        },
                        onStepFour = { govId, cityId ->
                            registerFormData.value = registerFormData.value.copy(
                                govId = govId,
                                cityId = cityId
                            )
                            activeStep.value = StepCount.Five

                        },
                        onStepFive = { pass ->
                            registerFormData.value = registerFormData.value.copy(
                                password = pass,
                            )
                            signupForm(registerFormData.value)

                        }
                    ) {
                        handleBackWithCurrentStep(activeStep, isDialogOpened)
                    }
                    ListenToChanges(activeStep)

                    DismissDialog(isOpened = isDialogOpened, onConfirmClicked = {
                        findNavController().popBackStack()
                    })
                    customBackStackButton(activeStep, isDialogOpened)*/
                }

            }
        }
    }

    private fun customBackStackButton(
        activeStep: MutableState<StepCount>,
        isDialogOpened: MutableState<Boolean>
    ) {
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    handleBackWithCurrentStep(activeStep, isDialogOpened)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun handleBackWithCurrentStep(
        activeStep: MutableState<StepCount>,
        isDialogOpened: MutableState<Boolean>
    ) {
        when (activeStep.value) {
            StepCount.One -> {
                isDialogOpened.value = true
            }
            StepCount.Two -> activeStep.value = StepCount.One
            StepCount.Three -> activeStep.value = StepCount.Two
            StepCount.Four -> activeStep.value = StepCount.Three
            StepCount.Five -> activeStep.value = StepCount.Four
        }
    }

    private fun signupForm(signupData: RegisterIntent.Signup) {
        requireContext().hideKeypad(requireView())
        lifecycleScope.launch {
            viewModel.intentChannel.send(
                signupData
            )
        }
    }

    private fun validateEmail(email: String) {
        requireContext().hideKeypad(requireView())
        lifecycleScope.launch {
            viewModel.intentChannel.send(
                RegisterIntent.ValidateEmail(email = email)
            )
        }
    }

    private fun validatePhone(phone: String) {
        requireContext().hideKeypad(requireView())
        lifecycleScope.launch {
            viewModel.intentChannel.send(
                RegisterIntent.ValidatePhone(phone = phone)
            )
        }
    }

    @Composable
    private fun ListenToChanges(activeStep: MutableState<StepCount>) {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value


        LoadingDialog(stateValue.isLoading)

        if (stateValue.isValidPhone) {
            if (stateValue.phone != null) {
                sendVerificationCode(stateValue.phone, activeStep)
                //activeStep.value = StepCount.Two
            }

        }
        if (stateValue.isValidEmail) {
            activeStep.value = StepCount.Four
        }


        if (stateValue.isSuccess && stateValue.data != null) {
            requireActivity().apply {
                openReportActivity()
            }
        }
        if (stateValue.networkError != null) {
            stateValue.networkError.ShowNetworkErrorSnakeBarUi(
                view = requireView()
            )

        }
    }

    /**
     * Phone OTP
     */

    private fun sendVerificationCode(phone: String, activeStep: MutableState<StepCount>) {
        val mPhone = "+20$phone"
        // this method is used for getting
        // OTP on user phone number.
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mPhone) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                // below method is used when
                // OTP is sent from Firebase
                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    // when we receive the OTP it
                    // contains a unique id which
                    // we are storing in our string
                    // which we have already created.
                    verificationId = s
                    activeStep.value = StepCount.Two
                }

                // this method is called when user
                // receive OTP from Firebase.
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    // below line is used for getting OTP code
                    // which is sent in phone auth credentials.
                    val code = phoneAuthCredential.smsCode

                    // checking if the code
                    // is null or not.
                    if (code != null) {
                        // if the code is not null then
                        // we are setting that code to
                        // our OTP edittext field.
                        //edtOTP.setText(code)
                        otpState.value = code

                        // after setting this code
                        // to OTP edittext field we
                        // are calling our verifycode method.
                        verifyCode(code) {
                            activeStep.value = StepCount.Three
                        }
                    }
                }

                // this method is called when firebase doesn't
                // sends our OTP code due to any error or issue.
                override fun onVerificationFailed(e: FirebaseException) {
                    // displaying error message with firebase exception.
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.

                    val message = if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        "Invalid request ${e.message}"

                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        "SMS quota for Mafqud has been exceeded"

                    } else {
                        "Unknown error"
                    }
                    // Show a message and update the UI
                    // ...
                    LogMe.i("onVeriFailed", message)
                    requireContext().showToast(
                        message,
                    )
                }
            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(code: String, onSuccessOtp: () -> Unit) {
        verifyOtpLoadingIntent(isLoading = true)
        // below line is used for getting getting
        // credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential, onSuccessOtp)
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccessOtp: () -> Unit,
    ) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                verifyOtpLoadingIntent(isLoading = false)
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    onSuccessOtp()

                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    requireContext().showToast(
                        getString(R.string.error_code),
                    )
                }
            }
    }

    private fun verifyOtpLoadingIntent(isLoading: Boolean) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(RegisterIntent.VerifyOTP(loading = isLoading))
        }
    }

}