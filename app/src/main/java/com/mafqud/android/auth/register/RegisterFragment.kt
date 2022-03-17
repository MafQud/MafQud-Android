package com.mafqud.android.auth.register

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.compose.showAreYouSureDialog
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBarUi
import com.mafqud.android.util.other.hideKeypad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private val viewModel: RegisterViewModel by viewModels()

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
                val registerFormData = remember {
                    mutableStateOf(RegisterIntent.Signup())
                }
                val activeStep = remember {
                    mutableStateOf(StepCount.One)
                }
                MafQudTheme {
                    RegisterScreen(
                        registerFormData,
                        activeStep,
                        onStepOne = { phone ->
                            registerFormData.value = registerFormData.value.copy(
                                phone = phone

                            )
                            validatePhone(phone = phone)

                        },
                        onStepTwo = { otp ->
                            activeStep.value = StepCount.Three

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
                        handleBackWithCurrentStep(activeStep)
                    }
                    ListenToChanges(activeStep)
                }

                customBackStackButton(activeStep)
            }
        }
    }

    private fun customBackStackButton(activeStep: MutableState<StepCount>) {
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    handleBackWithCurrentStep(activeStep)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun handleBackWithCurrentStep(activeStep: MutableState<StepCount>) {
        when (activeStep.value) {
            StepCount.One ->{
                requireContext().showAreYouSureDialog {
                    findNavController().popBackStack()
                }
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
            activeStep.value = StepCount.Two
        }
        if (stateValue.isValidEmail) {
            activeStep.value = StepCount.Four
        }


        if (stateValue.isSuccess && stateValue.data != null) {
            requireActivity().apply {
                openHomeActivity()
            }
        }
        if (stateValue.networkError != null) {
            stateValue.networkError.ShowNetworkErrorSnakeBarUi(
                view = requireView()
            )

        }
    }

    private fun openHomeActivity() {
        startActivity(
            Intent(
                requireContext(), HomeActivity::class.java
            )
        )
        requireActivity().finish()
    }


}