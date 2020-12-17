package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class msibook_msipay_register4_set_pay_pwd extends AppCompatActivity {

    private LinearLayout linear_bottom;
    private Context mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_msipay_register4_set_pay_pwd);

        mContent = this;

        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);

        linear_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msipay = new Intent(mContent,msibook_msipay_main.class);

                mContent.startActivity(msipay);

            }
        });

    }
}