package com.mafqud.android.auth

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.mafqud.android.R
import com.mafqud.android.auth.intro.IntroScreen
import com.mafqud.android.auth.login.LoginIntent
import com.mafqud.android.auth.login.LoginScreen
import com.mafqud.android.auth.login.LoginViewModel
import com.mafqud.android.auth.register.RegisterIntent
import com.mafqud.android.auth.register.RegisterScreen
import com.mafqud.android.auth.register.RegisterViewModel
import com.mafqud.android.auth.register.StepCount
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.ui.compose.DismissDialog
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.other.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    sealed class AuthScreen(val route: String) {
        object Intro : AuthScreen("intro")
        object Login : AuthScreen("login")
        object Register : AuthScreen("register")
    }


    lateinit var mAuth: FirebaseAuth

    // string for storing our verification ID
    private var verificationId: String = ""

    //OTP remember
    private val otpState: MutableState<String> = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarColor(resources.getColor(R.color.blue))
        setContent {
            MafQudTheme {
                /*  // Update the status bars to be translucent
                  val systemUiController = rememberSystemUiController()
                  val useDarkIcons = isSystemInDarkTheme()
                  SideEffect {
                      systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
                  }*/
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState,
                ) {
                    val navController = rememberNavController()
                    NavigationAuth(navController, scaffoldState)
                }


            }
        }
    }

    @Composable
    private fun NavigationAuth(navController: NavHostController, scaffoldState: ScaffoldState) {
        NavHost(navController = navController, startDestination = AuthScreen.Intro.route) {
            composable(AuthScreen.Intro.route) {
                IntroRouter(navController)
            }
            composable(AuthScreen.Login.route) {
                LoginRouter(navController, scaffoldState)
            }
            composable(AuthScreen.Register.route) {
                val registerViewModel: RegisterViewModel by viewModels()
                RegisterRouter(
                    navController, scaffoldState, registerViewModel,
                )
            }
        }
    }

    @Composable
    private fun RegisterRouter(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        registerViewModel: RegisterViewModel,
    ) {
        // variable for FirebaseAuth class
        mAuth = FirebaseAuth.getInstance()

        val isDialogOpened = remember {
            mutableStateOf(false)
        }

        val state = registerViewModel.stateChannel.collectAsState()
        val stateValue = state.value


        RegisterScreen(
            scaffoldState = scaffoldState,
            stateValue = stateValue,
            otpState = otpState,
            onStepOne = { phone ->
                otpState.value = ""
                validatePhone(registerViewModel = registerViewModel, phone = phone)
            },
            onStepTwo = { otp ->
                otpState.value = otp
                verifyCode(registerViewModel, otp) {
                    sendNextActiveStep(registerViewModel, StepCount.Three)
                }
            },
            onStepThree = { name ->
                sendUserNameIntent(registerViewModel, name)
            },
            onStepFour = { govId, cityId ->
                sendLocationData(registerViewModel, govId, cityId)
            },
            onStepFive = { pass ->
                savePasswordData(registerViewModel, pass)

            }, onBackPressed = {
                handleBackWithCurrentStep(stateValue.stepCount, isDialogOpened, registerViewModel)
            }, onSuccessRegister = {
                openReportActivity()
            },
            sendVerificationCode = { phone ->
                sendVerificationCode(registerViewModel, phone)

            }, onGovSelected = {
                requestCities(registerViewModel, it)
            }
        )

        DismissDialog(isOpened = isDialogOpened, onConfirmClicked = {
            resetInitialUiState(registerViewModel)
            navController.popBackStack()
        })

        customBackStackButton(stateValue.stepCount, isDialogOpened, registerViewModel)

    }

    private fun resetInitialUiState(registerViewModel: RegisterViewModel) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.Clear)
        }
    }

    private fun savePasswordData(registerViewModel: RegisterViewModel, pass: String) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.SavePassword(pass))
        }
    }

    private fun sendLocationData(registerViewModel: RegisterViewModel, govId: Int, cityId: Int) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.SaveLocation(govId, cityId))
        }
    }

    private fun requestCities(registerViewModel: RegisterViewModel, it: Int) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.GetCities(it))
        }
    }

    private fun sendUserNameIntent(registerViewModel: RegisterViewModel, name: String) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.SaveName(name))
        }
    }

    private fun sendNextActiveStep(registerViewModel: RegisterViewModel, nextStep: StepCount) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.NextStep(nextStep))
        }
    }

    private fun sendVerificationCode(
        registerViewModel: RegisterViewModel,
        phone: String,
    ) {
        val mPhone = "+20$phone"
        // this method is used for getting
        // OTP on user phone number.
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mPhone) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                // below method is used when
                // OTP is sent from Firebase
                override fun onCodeSent(
                    s: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(s, forceResendingToken)
                    // when we receive the OTP it
                    // contains a unique id which
                    // we are storing in our string
                    // which we have already created.
                    verificationId = s
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
                        verifyCode(registerViewModel, code) {
                            sendNextActiveStep(registerViewModel, StepCount.Three)
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
                        sendNextActiveStep(registerViewModel, StepCount.Three)
                        // The SMS quota for the project has been exceeded
                        "SMS quota for Mafqud has been exceeded"

                    } else {
                        e.localizedMessage
                    }
                    // Show a message and update the UI
                    // ...
                    LogMe.i("onVeriFailed", message)
                    showToast(message)
                }
            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(registerViewModel: RegisterViewModel, code: String, onSuccessOtp: () -> Unit) {
        verifyOtpLoadingIntent(registerViewModel, isLoading = true)
        // below line is used for getting getting
        // credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(registerViewModel, credential, onSuccessOtp)
    }

    private fun signInWithCredential(
        registerViewModel: RegisterViewModel,
        credential: PhoneAuthCredential,
        onSuccessOtp: () -> Unit,
    ) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                verifyOtpLoadingIntent(registerViewModel = registerViewModel, isLoading = false)
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    onSuccessOtp()

                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    showToast(getString(R.string.error_code))
                }
            }
    }

    private fun verifyOtpLoadingIntent(registerViewModel: RegisterViewModel, isLoading: Boolean) {
        lifecycleScope.launchWhenCreated {
            registerViewModel.intentChannel.send(RegisterIntent.VerifyOTP(loading = isLoading))
        }
    }

    @Composable
    private fun customBackStackButton(
        activeStep: StepCount,
        isDialogOpened: MutableState<Boolean>,
        registerViewModel: RegisterViewModel
    ) {
        BackHandler {
            handleBackWithCurrentStep(activeStep, isDialogOpened, registerViewModel)
        }
    }

    private fun handleBackWithCurrentStep(
        activeStep: StepCount,
        isDialogOpened: MutableState<Boolean>,
        registerViewModel: RegisterViewModel
    ) {
        when (activeStep) {
            StepCount.One -> {
                isDialogOpened.value = true
            }
            StepCount.Two -> sendNextActiveStep(registerViewModel, StepCount.One)
            StepCount.Three -> sendNextActiveStep(registerViewModel, StepCount.Two)
            StepCount.Four -> sendNextActiveStep(registerViewModel, StepCount.Three)
            StepCount.Five -> sendNextActiveStep(registerViewModel, StepCount.Four)
        }
    }

    private fun validateEmail(registerViewModel: RegisterViewModel, email: String) {
        //requireContext().hideKeypad(requireView())
        lifecycleScope.launch {
            registerViewModel.intentChannel.send(
                RegisterIntent.ValidateEmail(email = email)
            )
        }
    }

    private fun validatePhone(registerViewModel: RegisterViewModel, phone: String) {
        //requireContext().hideKeypad(requireView())
        lifecycleScope.launch {
            registerViewModel.intentChannel.send(
                RegisterIntent.ValidatePhone(phone = phone)
            )
        }
    }


    @Composable
    private fun LoginRouter(navController: NavHostController, scaffoldState: ScaffoldState) {
        val loginViewModel: LoginViewModel by viewModels()
        LoginScreen(scaffoldState = scaffoldState, viewModel = loginViewModel,
            onBackPressed = {
                navController.popBackStack()
            }, onNextPressed = {
                // login after done clicked in keypad
                loginIntent(
                    loginViewModel,
                    phone = it.phone,
                    password = it.password
                )
            }, onLoginSuccess = {
                openReportActivity()
            })
    }

    @Composable
    private fun IntroRouter(navController: NavHostController) {
        IntroScreen(onLogin = {
            navController.navigate(AuthScreen.Login.route)
        }, onSignUp = {
            navController.navigate(AuthScreen.Register.route)
        })
    }

    private fun loginIntent(
        viewModel: LoginViewModel,
        phone: String,
        password: String,
    ) {
        //hideKeypad(this.view)
        lifecycleScope.launch {
            viewModel.intentChannel.send(
                LoginIntent.PhoneLogin(
                    phone = phone,
                    password = password
                )
            )
        }
    }
}