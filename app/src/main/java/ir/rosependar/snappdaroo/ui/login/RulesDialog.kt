package ir.rosependar.snappdaroo.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ir.rosependar.snappdaroo.R
import kotlinx.android.synthetic.main.rules_dialog_fragment.*

class RulesDialog : DialogFragment() {
    interface RulesListener {
        fun onRulesAccepted()
    }
    companion object {
        var listener: RulesListener? = null
        fun newInstance() = RulesDialog()
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rules_dialog_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txt_rules.setText(Html.fromHtml("داروپیک\n" +
                "               با تاکید بر احترامی که برای حریم شخصی کاربران قائل است، برای خرید، ثبت نظر یا استفاده از برخی امکانات وب\u200Cسایت اطلاعاتی را از کاربران درخواست می\u200Cکند تا بتواند خدماتی امن و مطمئن را به کاربران ارائه دهد. برای پردازش و ارسال سفارش، اطلاعاتی مانند آدرس، شماره تلفن و ایمیل مورد نیاز است و از آنجا که کلیه فعالیت\u200Cهای این داروخانه ی مجازی قانونی و مبتنی بر قوانین تجارت الکترونیک صورت می\u200Cگیرد و طی فرایند خرید، فاکتور رسمی و بنا به درخواست مشتریان حقوقی گواهی ارزش افزوده صادر می\u200Cشود، از این رو وارد کردن اطلاعاتی مانند نام و کد ملی برای اشخاص حقیقی یا کد اقتصادی و شناسه ملی برای خریدهای سازمانی لازم است. یادآوری می\u200Cشود آدرس ایمیل و تلفن\u200Cهایی که مشتری در پروفایل خود ثبت می\u200Cکند، تنها آدرس ایمیل و تلفن\u200Cهای رسمی و مورد تایید مشتری است و تمام مکاتبات و پاسخ\u200Cهای شرکت از طریق آنها صورت می\u200Cگیرد.\n" +
                "               <br>\n" +
                "               بنابراین درج آدرس، ایمیل و شماره تماس\u200Cهای همراه و ثابت توسط مشتری، به منزله مورد تایید بودن صحت آنها است و در صورتی که موارد فوق به صورت صحیح یا کامل درج نشده باشد، داروپیک جهت اطمینان از صحت و قطعیت ثبت سفارش می\u200Cتواند از مشتری، اطلاعات تکمیلی و بیشتری درخواست کند.\n" +
                "               مشتریان می\u200Cتوانند نام، آدرس و تلفن شخص دیگری را برای تحویل گرفتن سفارش وارد کنند و اسنپ دارو تنها برای ارسال همان سفارش، از این اطلاعات استفاده خواهد کرد.\n" +
                "               <br>\n" +
                "               داروپیک همچنین   \n" +
                "               ممکن است برای ارتباط با مشتریان، بهینه\u200Cسازی محتوای وب\u200Cسایت و تحقیقات بازاریابی از برخی اطلاعات استفاده کند و برای اطلاع\u200Cرسانی رویدادها و اخبار، خدمات و سرویس\u200Cهای ویژه یا پروموشن\u200Cها، برای اعضای وب\u200Cسایت ایمیل یا پیامک ارسال نماید. در صورتی که کاربران تمایل به دریافت اینگونه ایمیل\u200Cها و پیامک\u200Cها نداشته باشند، می\u200Cتوانند عضویت دریافت خبرنامه اسنپ دارو را در پروفایل خود لغو کنند. عدم اقدام جهت لغو، به منزله ی موافقت ضمنی با دریافت ایمیل\u200Cها و پیام\u200Cها و سلب حق اعتراض می\u200Cباشد."))
        btn_accept.setOnClickListener {
            listener?.onRulesAccepted()
            dismissAllowingStateLoss()
        }
    }

}