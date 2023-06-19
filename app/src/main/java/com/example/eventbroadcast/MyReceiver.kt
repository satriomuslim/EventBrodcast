package com.example.eventbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

class MyReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = MyReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        try {
            if (bundle != null) {
                val pdusObj = bundle.get("pdus") as Array<*>
                for (aPdusObj in pdusObj){
                    val currentMessage = getIncomingMessage(aPdusObj as Any, bundle)
                    val senderNum = currentMessage.displayOriginatingAddress
                    val message = currentMessage.displayMessageBody
                    Log.e(TAG, "senderNum $senderNum; message: $message")

                    val showSmsIntent = Intent(context, SmsReceiverActivity::class.java)
                    showSmsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, senderNum)
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message)
                    context.startActivity(showSmsIntent)
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception smsReceiver $e")
        }

    }

    private fun getIncomingMessage(aObject: Any, bundle: Bundle): SmsMessage {
        val currentSms: SmsMessage
        val format = bundle.getString("Format")
        currentSms = SmsMessage.createFromPdu(aObject as ByteArray, format)
        return currentSms
    }


}