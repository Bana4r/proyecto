package com.example.proyecto

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class AddSubscriptionActivity : AppCompatActivity() {

    private lateinit var subscriptionDao: SubscriptionDao
    private var userId: Int = -1
    private lateinit var category: String
    private lateinit var serviceName: String
    private lateinit var plan: Plan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subscription)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { finish() }

        subscriptionDao = AppDatabase.getDatabase(this).subscriptionDao()

        // Retrieve data
        userId = intent.getIntExtra("USER_ID", -1)
        category = intent.getStringExtra("CATEGORY") ?: ""
        serviceName = intent.getStringExtra("SERVICE_NAME") ?: ""
        plan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("PLAN", Plan::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("PLAN")
        } ?: Plan("", 0.0, "")

        if (userId == -1 || category.isEmpty() || serviceName.isEmpty() || plan.name.isEmpty()) {
            Toast.makeText(this, "Error: Faltan datos.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Setup UI
        findViewById<TextView>(R.id.service_name_text_view).text = serviceName
        findViewById<TextView>(R.id.plan_details_text_view).text = String.format("%s - $%.2f / %s", plan.name, plan.price, plan.billingPeriod)
        findViewById<Button>(R.id.save_button).setOnClickListener { showPaymentMethodDialog() }
    }

    private fun showPaymentMethodDialog() {
        val paymentMethods = arrayOf("Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia", "Efectivo")
        AlertDialog.Builder(this)
            .setTitle("Selecciona una Forma de Pago")
            .setItems(paymentMethods) { _, which ->
                when (which) {
                    0, 1 -> showCardPaymentDialog()
                    2 -> showTransferPaymentDialog()
                    3 -> showCashPaymentDialog()
                }
            }.show()
    }

    private fun showCardPaymentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_card_payment, null)
        AlertDialog.Builder(this)
            .setTitle("Ingresa los Datos de la Tarjeta")
            .setView(dialogView)
            .setPositiveButton("Pagar") { _, _ -> saveSubscriptionAndFinish() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showTransferPaymentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_transfer_payment, null)
        val reference = String.format("%04d-%04d-%04d", Random.nextInt(10000), Random.nextInt(10000), Random.nextInt(10000))
        dialogView.findViewById<TextView>(R.id.transfer_reference_text_view).text = reference

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Finalizar") { _, _ -> saveSubscriptionAndFinish() }
            .show()
    }

    private fun showCashPaymentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cash_payment, null)
        val amount = plan.price
        dialogView.findViewById<TextView>(R.id.amount_text_view).text = String.format("Monto a Pagar: $%.2f MXN", amount)

        val qrCodeImageView = dialogView.findViewById<ImageView>(R.id.qr_code_image_view)
        try {
            val qrCodeBitmap = generateQrCode(amount.toString())
            qrCodeImageView.setImageBitmap(qrCodeBitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
            // Optionally, show an error message or a placeholder
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Finalizar") { _, _ -> saveSubscriptionAndFinish() }
            .show()
    }

    private fun generateQrCode(text: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    private fun saveSubscriptionAndFinish() {
        val calendar = Calendar.getInstance()
        if (plan.billingPeriod == "mensual") calendar.add(Calendar.MONTH, 1) else calendar.add(Calendar.YEAR, 1)
        val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val renewalDate = dateFormat.format(calendar.time)

        val subscription = Subscription(
            userId = userId,
            serviceName = serviceName,
            planName = plan.name,
            renewalDate = renewalDate,
            cost = plan.price,
            category = category
        )

        lifecycleScope.launch {
            subscriptionDao.insert(subscription)
            runOnUiThread {
                Toast.makeText(this@AddSubscriptionActivity, "Pago procesado, suscripción guardada", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AddSubscriptionActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            }
        }
    }
}