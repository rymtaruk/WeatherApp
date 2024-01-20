package com.reynard.weatherapp.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.reynard.core.di.utils.ViewModelFactory
import com.reynard.weatherapp.di.injector.AppInjector
import com.reynard.weatherapp.utils.ConnectionUtil
import javax.inject.Inject

abstract class AppBaseActivity<vb : ViewBinding, vm : AppBaseViewModel>(
    private val bindingFactory: (LayoutInflater) -> vb,
    private val viewModelClass: Class<vm>
) : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var viewModel: vm

    private var _binding: vb? = null
    protected val binding: vb get() = _binding!!

    private var _connectivityManager: ConnectivityManager? = null
    private val connectivityManager get() = _connectivityManager!!

    private val connectionHandler = ConnectionUtil.callback {
        Snackbar.make(binding.root, "Opps! no internet connection :(", Snackbar.LENGTH_SHORT).show()
    }

    protected abstract fun initView()
    protected abstract fun onViewCreated()
    protected abstract fun onObservableViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector.getInstance(this).androidInjector().inject(this)
        super.onCreate(savedInstanceState)
        _binding = bindingFactory.invoke(layoutInflater)
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass]
        setContentView(binding.root)

        if (_connectivityManager == null) {
            _connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        initView()
        onViewCreated()
        onObservableViewModel()
        showDefaultError()
    }

    private fun showDefaultError(){
        viewModel.defaultError.observe(this) {
            if (ConnectionUtil.isNotConnectedWifiAndCellular(this@AppBaseActivity)){
                Snackbar.make(binding.root, "Opps! no internet connection :(", Snackbar.LENGTH_SHORT).show()
                return@observe
            }

            Snackbar.make(binding.root, "Opps! $it", Snackbar.LENGTH_SHORT).show()
        }

        viewModel.defaultUnauthorized.observe(this) {
            if (ConnectionUtil.isNotConnectedWifiAndCellular(this@AppBaseActivity)){
                Snackbar.make(binding.root, "Opps! no internet connection :(", Snackbar.LENGTH_SHORT).show()
                return@observe
            }

            Snackbar.make(binding.root, "Opps! $it", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        connectivityManager.registerDefaultNetworkCallback(connectionHandler)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(connectionHandler)
    }
}