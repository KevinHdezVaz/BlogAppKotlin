package com.kevin.courseApp.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.hideKeyboard
import com.kevin.courseApp.data.remote.auth.AuthDataSource
import com.kevin.courseApp.databinding.FragmentLoginBinding
import com.kevin.courseApp.domain.auth.AuthRepoImplements
import com.kevin.courseApp.presentation.auth.AuthViewModel
import com.kevin.courseApp.presentation.auth.LoginScreeViewModelFactory


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    var simpleVideoView: VideoView? = null
    var mediaControls: MediaController? = null

    private lateinit var googleSignInClient : GoogleSignInClient

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewmodel by viewModels<AuthViewModel> {
        LoginScreeViewModelFactory(AuthRepoImplements(AuthDataSource()))
    }

    //forma de inicializar una propiedad "perezosa"
    /**
     * la propiedad solo se inicializara la primera vz
     * que se acceda a ella y se guardara en memoria
     *
     * */



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
isUserLoggin()
        duLogin()
        GotosignUp()
        videoFondo()
        gotoInvitado()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext() , gso)

        binding.btnGoogle.setOnClickListener{
            signInGoogle()
            Toast.makeText(requireContext(),"THIS",Toast.LENGTH_SHORT).show()
        }



    }

    private fun gotoInvitado() {

        binding.btnInvitado.setOnClickListener{

            binding.editTextEmail.setText("curso@gmail.com")
            binding.editTextPassword.setText("prueba123")
            signIn(binding.editTextEmail.text.toString(),binding.editTextPassword.text.toString())
        }
    }


    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(requireContext(), task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
               findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            }else{
                Toast.makeText(requireContext(), it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun videoFondo() {


        if (mediaControls == null) {
            mediaControls = MediaController(context)
            mediaControls!!.setAnchorView(simpleVideoView)
        }
        binding.vvFondo.setOnPreparedListener { mp ->
            val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
            val screenRatio: Float =
                binding.vvFondo.width / binding.vvFondo.height.toFloat()
            val scaleX = videoRatio / screenRatio
            if (scaleX >= 1f) {
                binding.vvFondo.scaleX = scaleX
            } else {
                binding.vvFondo.scaleY = 1f / screenRatio
            }
        }

        binding.vvFondo.setVideoURI(Uri.parse("android.resource://" + "com.kevin.courseApp" + "/" + R.raw.videonfoo))
        binding.vvFondo.start()
        binding.vvFondo.setOnCompletionListener { binding.vvFondo.start() }
        binding.vvFondo.setOnErrorListener { _, _, _ ->
            false
        }
    }





    fun isUserLoggin(){
       /**
        * Si el usuario essta logueado (diferente de nulo) pasar al home Fragment
        * */

        firebaseAuth.currentUser?.let {

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }


    fun duLogin(){
        binding.btnSignin.setOnClickListener{
            //trim : sirve para que cuando se encuentran espacios adelante o atras, no se
            // tomen en cuenta, solo el texto escrito.

            it.hideKeyboard()

            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            validateCredentials(email,password)
            signIn(email,password)
        }
    }

    private fun validateCredentials(email:String, password:String){
        if(email.isEmpty()){
            binding.editTextEmail.error = "Email esta vacio"
            return
        }
        if(password.isEmpty()){
            binding.editTextPassword.error = "Email esta vacio"
            return
        }
    }
    private fun GotosignUp(){

        binding.txtSignup.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }
     }
    private fun signIn(email: String,password: String){

        viewmodel.SignIn(email,password).observe(viewLifecycleOwner, Observer {
            when(it){
                is  Result.Loading ->{
                    binding.progresBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }

                is  Result.Success->{
                    binding.progresBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)


                }

                is  Result.Failure ->{
                    binding.progresBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(requireContext(), "Error ${it.exception}", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        //        mCurrentVideoPosition = mMediaPlayer.currentPosition;
        binding.vvFondo.pause()

    }

    override fun onResume() {
        super.onResume()
        binding.vvFondo.start()
    }
}