package com.harjot.razerpaymentgateway

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.harjot.razerpaymentgateway.databinding.ActivityMainBinding
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

// task is how to send particular user to pay a particular upi id

class MainActivity : AppCompatActivity(), PaymentResultWithDataListener,ExternalWalletListener {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRedirect.setOnClickListener{
            startPayment()
        }
    }
    private fun startPayment(){
        Checkout.preload(this)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID("rzp_test_c51XgNNFUQLpLP")

        try{
            val options = JSONObject()
            options.put("name","Harjot")
            options.put("image",R.drawable.ic_launcher_background)
            options.put("theme.color","#3399cc")
            options.put("currency","INR")
            options.put("amount",binding.etAmount.text.toString())

            val retryObj = JSONObject()
            retryObj.put("enabled",true)
            retryObj.put("max_count",4)
            options.put("retry",retryObj)

            val prefill = JSONObject()
            prefill.put("email","harjot@gmail.com")
            prefill.put("contact","9191919191")
            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment successfull", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error: $p1", Toast.LENGTH_SHORT).show()
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {

    }
}