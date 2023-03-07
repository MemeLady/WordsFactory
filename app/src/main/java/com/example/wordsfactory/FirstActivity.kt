package com.example.wordsfactory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.wordsfactory.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private var binding: ActivityFirstBinding?=null
    private var viewPager2: ViewPager2?=null
    private val pager2CallBack=object:ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            if(position==PageLists.introSlides.size-1){
                binding?.controllerBtn?.text="Let's Start"
                binding?.controllerBtn?.setOnClickListener {
                    nextActivity()
                }
            }
            else{
                binding?.controllerBtn?.text="Next"
                binding?.controllerBtn?.setOnClickListener{
                    viewPager2?.currentItem=position+1
                }
            }
        }
    }

    private fun nextActivity()
    {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupViewPager(binding!!)

        binding!!.skipBtn.setOnClickListener {
            nextActivity()
        }

    }

    private fun setupViewPager(binding: ActivityFirstBinding){
        val adapter=IntroAdapter(PageLists.introSlides)
        viewPager2=binding.viewPager
        viewPager2?.adapter=adapter
        viewPager2?.registerOnPageChangeCallback(pager2CallBack)
        binding.dotsIndicator.setViewPager2(viewPager2!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager2?.unregisterOnPageChangeCallback(pager2CallBack)
    }
}