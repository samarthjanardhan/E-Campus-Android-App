package com.example.samarth.ecampus;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise here");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription("This is demo version made by Samarth Janardhan and Sahil Sachdev")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("andstd1610@gmail.com")
                .addWebsite("http://cms.sinhgad.edu/sinhgad_engineering_institutes/sits_narhetechnicalcampus/sits_nt_aboutus.aspx")
                .addFacebook("Sinhgad Institute of Technology and Science,Narhe (S.I.T.S)")
                .addTwitter("twitter")
                .addYoutube("UCC3am9aYmnJQsrPDeV1DlnQ?view_as=subscriber")
                .addItem(createCopyright())
                .create();

        setContentView(aboutPage);
    }

    private Element createCopyright() {

        Element copyright = new Element();
        final String copyrightString = String.format("Copyright %d by SITS", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(AboutUs.this, copyrightString,Toast.LENGTH_SHORT).show();

            }
        });

        return copyright;
    }
}
