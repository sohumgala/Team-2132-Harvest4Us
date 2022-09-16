package com.example.myfirstapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonEligibilityStatus
import kotlinx.android.synthetic.main.activity_payment_button_quick_start.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PaymentButtonQuickStartActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    var responseString = ""
    private val tag = javaClass.simpleName
    private var username: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_button_quick_start)

        username = intent.getStringExtra("username")

        paymentButton.onEligibilityStatusChanged = { buttonEligibilityStatus: PaymentButtonEligibilityStatus ->
            Log.v(tag, "OnEligibilityStatusChanged")
            Log.d(tag, "Button eligibility status: $buttonEligibilityStatus")
        }
        setupPaymentButton()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupPaymentButton() {
        println("in")
        var extras: Bundle? = intent.extras
        var total: String = extras?.getString("total").toString()
        paymentButton.setup(
            createOrder = CreateOrder { createOrderActions ->
                Log.v(tag, "CreateOrder")
                createOrderActions.create(
                    Order.Builder()
                        .appContext(
                            AppContext(
                                userAction = UserAction.PAY_NOW
                            )
                        )
                        .intent(OrderIntent.CAPTURE)
                        .purchaseUnitList(
                            listOf(
                                PurchaseUnit.Builder()
                                    .amount(
                                        Amount.Builder()
                                            .value(total)
                                            .currencyCode(CurrencyCode.USD)
                                            .build()
                                    )
                                    .build()
                            )
                        )
                        .build()
                        .also { Log.d(tag, "Order: $it") }
                )
            },
            onApprove = OnApprove { approval ->
                Log.v(tag, "OnApprove")
                Log.d(tag, "Approval details: $approval")
                approval.orderActions.capture { captureOrderResult ->
                    Log.v(tag, "Capture Order")
                    Log.d(tag, "Capture order result: $captureOrderResult")
                }
                val sdf = SimpleDateFormat("yyyy-mm-dd")
                val current = sdf.format(Date())
                // run("https://crwpdbho85.execute-api.us-east-1.amazonaws.com/dev/checkout?username=fff&date=$current&total_cost=${total.toFloat()}")
                // setContentView(R.layout.activity_success)
                val i = Intent(this, Success::class.java)
                i.putExtra("username", username)
                startActivity(i)
            },
            onCancel = OnCancel {
                Log.v(tag, "OnCancel")
                Log.d(tag, "Buyer cancelled the checkout experience.")
            },
            onError = OnError { errorInfo ->
                Log.v(tag, "OnError")
                Log.d(tag, "Error details: $errorInfo")
            }
        )
    }

    companion object {
        fun startIntent(context: Context): Intent {
            return Intent(context, PaymentButtonQuickStartActivity::class.java)
        }
    }

    // runs an API call
    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                responseString = response.body?.string()!!
                println(responseString)
            }
        })
    }
}
