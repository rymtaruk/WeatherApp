package com.reynard.weatherapp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.reynard.core.di.utils.ViewModelFactory
import com.reynard.weatherapp.di.injector.AppInjector
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

    protected abstract fun initView()
    protected abstract fun onViewCreated()
    protected abstract fun onObservableViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector.getInstance(this).androidInjector().inject(this)
        super.onCreate(savedInstanceState)
        _binding = bindingFactory.invoke(layoutInflater)
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass]
        setContentView(binding.root)

        initView()
        onViewCreated()
        onObservableViewModel()
    }
}