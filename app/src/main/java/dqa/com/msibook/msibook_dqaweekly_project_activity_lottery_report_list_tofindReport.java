package dqa.com.msibook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class msibook_dqaweekly_project_activity_lottery_report_list_tofindReport extends AppCompatActivity {

    private ProgressDialog progressBar;

    private Context mContext;
    private DownloadFile_Data_Adapter mDownloadFile_Data_Adapter;
    private List<DownloadFile_Data_Item> DownloadFile_Data_Item_List = new ArrayList<DownloadFile_Data_Item>();

    private ListView mListView;

    private TextView textView_Result;
    private TextView textView_F_SeqNo;
    private TextView textView_F_Model;
    private TextView textView_F_Subject;

    private TextView textView_F_Author;
    private TextView textView_F_Progress;
    private TextView textView_F_TestResultID;
    private TextView textView_F_WorkHour;
    private TextView textView_F_CostSum;

    private TextView textView_F_Topic;
    private TextView textView_F_Stage;
    private TextView textView_F_WType;
    private TextView textView_F_PeopleSum;
    private TextView textView_F_Ver;

    private TextView textView_F_Result;
    private TextView textView_F_Env;
    private TextView textView_F_Comments;
    private TextView textView_F_Platform;
    private TextView textView_F_EQP;
    private TextView textView_F_Ref;
    private TextView textView_Lob;

    private String Set_F_SeqNo;
    private String Set_F_Model;
    private String Set_F_Subject;

    private String Set_F_Author;
    private String Set_F_Progress;
    private String Set_F_TestResultID;
    private String Set_F_WorkHour;
    private String Set_F_CostSum;

    private String Set_F_Topic;
    private String Set_F_Stage;
    private String Set_F_WType;
    private String Set_F_PeopleSum;
    private String Set_F_Ver;

    private String Set_F_Result;
    private String Set_F_Env;
    private String Set_F_Comments;
    private String Set_F_Platform;
    private String Set_F_EQP;
    private String Set_F_Ref;

    //*********
    private String Set_F_ModelID;
    private String Set_F_SDate;
    private String Set_F_EDate;
    private String Set_F_Common;
    private String Set_F_RegionID;
    private Boolean Set_F_IsAudit;
    private String Set_ModelLink;
    private String Set_IMSLink;
    private String Set_RSSLink;
    private Boolean Set_Result;
    private String Set_Improve;
    private String Set_Lob;
    //*********

    private  String getRSSNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_lottery_report_list_tofind_report);

        mContext = this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        textView_Result = (TextView) findViewById(R.id.textView_Result);
        textView_F_SeqNo = (TextView) findViewById(R.id.textView_F_SeqNo);
        textView_F_Model = (TextView) findViewById(R.id.textView_F_Model);
        textView_F_Subject = (TextView) findViewById(R.id.textView_F_Subject);

        textView_F_Author = (TextView) findViewById(R.id.textView_F_Author);
        textView_F_Progress = (TextView) findViewById(R.id.textView_F_Progress);
        textView_F_TestResultID = (TextView) findViewById(R.id.textView_F_TestResultID);
        textView_F_WorkHour = (TextView) findViewById(R.id.textView_F_WorkHour);
        textView_F_CostSum = (TextView) findViewById(R.id.textView_F_CostSum);

        textView_F_Topic = (TextView) findViewById(R.id.textView_F_Topic);
        textView_F_Stage = (TextView) findViewById(R.id.textView_F_Stage);
        textView_F_WType = (TextView) findViewById(R.id.textView_F_WType);
        textView_F_PeopleSum = (TextView) findViewById(R.id.textView_F_PeopleSum);
        textView_F_Ver = (TextView) findViewById(R.id.textView_F_Ver);

        textView_F_Result = (TextView) findViewById(R.id.textView_F_Result);
        textView_F_Env = (TextView) findViewById(R.id.textView_F_Env);
        textView_F_Comments = (TextView) findViewById(R.id.textView_F_Comments);
        textView_F_Platform = (TextView) findViewById(R.id.textView_F_Platform);
        textView_F_EQP = (TextView) findViewById(R.id.textView_F_EQP);
        textView_F_Ref = (TextView) findViewById(R.id.textView_F_Ref);
        textView_Lob = (TextView) findViewById(R.id.textView_Lob);

        getRSSNO = getIntent().getStringExtra("RSSNO");//週次

        Log.w("抓到的序號",getRSSNO);

        //Fail 點擊 (查看網頁版專案資訊)
        textView_Result.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_Result.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_Result.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        Dialog dialog=new AlertDialog.Builder(msibook_dqaweekly_project_activity_lottery_report_list_tofindReport.this)
                                .setTitle("不及格說明")
                                .setMessage(Set_Improve)//设置提示内容
                                //确定按钮
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                        Intent intent = new Intent();
//                                        intent.setAction(Intent.ACTION_VIEW);
//                                        intent.setData(Uri.parse(Set_ModelLink));
//                                        startActivity(intent);
                                    }
                                })
                                .create();//创建对话框
                        dialog.show();//显示对话框

                        return true;
                }

                return false;
            }
        });

        //專案 點擊 (查看網頁版專案資訊)
        textView_F_Model.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_F_Model.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_F_Model.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        Intent intent = new Intent(msibook_dqaweekly_project_activity_lottery_report_list_tofindReport.this, msibook_dqaweekly_project_info.class);

                        //給 專案名稱 MS-7788
                        intent.putExtra("F_Model_Name", Set_F_Model);//代 專案名稱下一頁
                        //給 專案代碼 12922
                        intent.putExtra("F_ModelID", Set_F_ModelID);//代專案代碼下一頁

                        startActivity(intent);

                        return true;
                }

                return false;
            }
        });

        //F_TestResultID 點擊 (查看IMS資訊)
        textView_F_TestResultID.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_F_TestResultID.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_F_TestResultID.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        Dialog dialog=new AlertDialog.Builder(msibook_dqaweekly_project_activity_lottery_report_list_tofindReport.this)
                                .setTitle("前往IMS頁面")
                                .setMessage("您正在離開週報APP\n前往IMS頁面")//设置提示内容
                                //确定按钮
                                .setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(Set_IMSLink));
                                        startActivity(intent);
                                    }
                                })
                                //取消按钮
                                .setNegativeButton("稍後再說", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.w("稍後再說","稍後再說");
                                    }
                                })
                                .create();//创建对话框
                        dialog.show();//显示对话框

                        return true;
                }

                return false;
            }
        });

        Find_RSS_Content(getRSSNO);

    }

    //從List找報告
    public void Find_RSS_Content(String RSSNO) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSS_Content?RSSNO=" + RSSNO, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0)
                    {
                        JSONObject IssueData = UserArray.getJSONObject(0);

                        if (IssueData.isNull("F_SeqNo")){
                            String F_SeqNo = "N/A";
                            Set_F_SeqNo = "N/A";
                        }else{
                            String F_SeqNo = String.valueOf(String.format("%010d", IssueData.getInt("F_SeqNo")));//報告序號 1
                            Set_F_SeqNo = F_SeqNo;
                        }
                        Find_RSS_File(Set_F_SeqNo);//查看附件
                        Log.w("Set_F_SeqNo",Set_F_SeqNo);

                        if (IssueData.isNull("F_Topic")){
                            String F_Topic = "N/A";
                            Set_F_Topic = "N/A";
                        }else{
                            String F_Topic = IssueData.getString("F_Topic");//工作性質
                            Set_F_Topic = F_Topic;
                        }
                        Log.w("Set_F_Topic",Set_F_Topic);

                        if (IssueData.isNull("F_Subject")){
                            String F_Subject = "N/A";
                            Set_F_Subject = "N/A";
                        }else{
                            String F_Subject = IssueData.getString("F_Subject");// 表頭 主旨 3
                            Set_F_Subject = F_Subject;
                        }
                        Log.w("Set_F_Subject",Set_F_Subject);

                        if (IssueData.isNull("F_Model")){
                            String F_Model = "N/A";
                            Set_F_Model = "N/A";
                        }else{
                            String F_Model = IssueData.getString("F_Model");//表頭 專案 2
                            Set_F_Model = F_Model;
                        }
                        Log.w("Set_F_Model",Set_F_Model);

                        if (IssueData.isNull("F_Author")){
                            String F_Author = "N/A";
                            Set_F_Author = "N/A";
                        }else{
                            String F_Author = IssueData.getString("F_Author"); //匯報人員  4
                            Set_F_Author = F_Author;
                        }
                        Log.w("Set_F_Author",Set_F_Author);

                        if (IssueData.isNull("F_Stage")){
                            String F_Stage = "N/A";
                            Set_F_Stage = "N/A";
                        }else{
                            String F_Stage = IssueData.getString("F_Stage"); //研發階段 10
                            Set_F_Stage = F_Stage;
                        }
                        Log.w("Set_F_Stage",Set_F_Stage);

                        if (IssueData.isNull("F_WType")){
                            String F_WType = "N/A";
                            Set_F_WType = "N/A";
                        }else{
                            String F_WType = IssueData.getString("F_WType"); //工作型態 11
                            Set_F_WType = F_WType;
                        }
                        Log.w("Set_F_WType",Set_F_WType);

                        if (IssueData.isNull("F_PeopleSum")){
                            String F_PeopleSum = "N/A";
                            Set_F_PeopleSum = "N/A";
                        }else{
                            String F_PeopleSum = String.valueOf(IssueData.getInt("F_PeopleSum")); // 人力 12
                            Set_F_PeopleSum = F_PeopleSum;
                        }
                        Log.w("Set_F_PeopleSum",Set_F_PeopleSum);

                        if (IssueData.isNull("F_ModelID")){
                            String F_ModelID = "N/A";
                            Set_F_ModelID = "N/A";
                        }else{
                            String F_ModelID = IssueData.getString("F_ModelID"); //
                            Set_F_ModelID = F_ModelID;
                        }
                        Log.w("Set_F_ModelID",Set_F_ModelID);

                        if (IssueData.isNull("F_SDate")){
                            String F_SDate = "N/A";
                            Set_F_SDate = "N/A";
                        }else{
                            String F_SDate = IssueData.getString("F_SDate");
                            Set_F_SDate = F_SDate;
                        }
                        Log.w("Set_F_SDate",Set_F_SDate);

                        if (IssueData.isNull("F_EDate")){
                            String F_EDate = "N/A";
                            Set_F_EDate = "N/A";
                        }else{
                            String F_EDate = IssueData.getString("F_EDate");
                            Set_F_EDate = F_EDate;
                        }
                        Log.w("Set_F_EDate",Set_F_EDate);

                        if (IssueData.isNull("F_WorkHour")){
                            String F_WorkHour = "N/A";
                            Set_F_WorkHour = "N/A";
                        }else{
                            String F_WorkHour = String.valueOf(IssueData.getDouble("F_WorkHour")); //工時  7
                            Set_F_WorkHour = F_WorkHour;
                        }
                        Log.w("Set_F_WorkHour",Set_F_WorkHour);

                        if (IssueData.isNull("F_TestResultID")){
                            String F_TestResultID = "N/A";
                            Set_F_TestResultID = "N/A";
                        }else{
                            String F_TestResultID = IssueData.getString("F_TestResultID"); //結果 6
                            Set_F_TestResultID = F_TestResultID;
                        }
                        Log.w("Set_F_TestResultID",Set_F_TestResultID);

                        if (IssueData.isNull("F_Ver")){
                            String F_Ver = "N/A";
                            Set_F_Ver = "N/A";
                        }else{
                            String F_Ver = String.valueOf(IssueData.getString("F_Ver"));//"F_SeqNo": 6159     ????
                            Set_F_Ver = F_Ver;
                        }
                        Log.w("Set_F_Ver",Set_F_Ver);

                        if (IssueData.isNull("F_Platform")){
                            String F_Platform = "N/A";
                            Set_F_Platform = "N/A";
                        }else{
                            String F_Platform = IssueData.getString("F_Platform");
                            Set_F_Platform = F_Platform;
                        }
                        Log.w("Set_F_Platform",Set_F_Platform);

                        if (IssueData.isNull("F_Env")){
                            String F_Env = "N/A";
                            Set_F_Env = "N/A";
                        }else{
                            String F_Env = IssueData.getString("F_Env");
                            Set_F_Env = F_Env;
                        }
                        Log.w("Set_F_Env",Set_F_Env);

                        if (IssueData.isNull("F_EQP")){
                            String F_EQP = "N/A";
                            Set_F_EQP = "N/A";
                        }else{
                            String F_EQP = IssueData.getString("F_EQP");
                            Set_F_EQP = F_EQP;
                        }
                        Log.w("Set_F_EQP",Set_F_EQP);

                        if (IssueData.isNull("F_Ref")){
                            String F_Ref = "N/A";
                            Set_F_Ref = "N/A";
                        }else{
                            String F_Ref = IssueData.getString("F_Ref");
                            Set_F_Ref = F_Ref;
                        }
                        Log.w("Set_F_Ref",Set_F_Ref);

                        if (IssueData.isNull("F_Result")){
                            String F_Result = "N/A";
                            Set_F_Result = "N/A";
                        }else{
                            String F_Result = IssueData.getString("F_Result"); //測試結果資料 14
                            Set_F_Result = F_Result;
                        }
                        Log.w("Set_F_Result",Set_F_Result);

                        if (IssueData.isNull("F_Comments")){
                            String F_Comments = "N/A";
                            Set_F_Comments = "N/A";
                        }else{
                            String F_Comments = IssueData.getString("F_Comments");
                            Set_F_Comments = F_Comments;
                        }
                        Log.w("Set_F_Comments",Set_F_Comments);

                        if (IssueData.isNull("F_Common")){
                            String F_Common = "N/A";
                            Set_F_Common = "N/A";
                        }else{
                            String F_Common = IssueData.getString("F_Common");
                            Set_F_Common = F_Common;
                        }
                        Log.w("Set_F_Common",Set_F_Common);

                        NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法
                        if (IssueData.isNull("F_CostSum")){
                            String F_CostSum = "N/A";
                            Set_F_CostSum = "N/A";
                        }else{
                            String F_CostSum =  String.valueOf(nf.format(IssueData.getInt("F_CostSum"))); //費用
                            Set_F_CostSum = F_CostSum;
                        }
                        Log.w("Set_F_CostSum",Set_F_CostSum);

                        if (IssueData.isNull("F_RegionID")){
                            String F_RegionID = "N/A";
                            Set_F_RegionID = "N/A";
                        }else{
                            String F_RegionID = IssueData.getString("F_RegionID");//
                            Set_F_RegionID = F_RegionID;
                        }
                        Log.w("Set_F_RegionID",Set_F_RegionID);

                        if (IssueData.isNull("F_Progress")){
                            String F_Progress = "N/A";
                            Set_F_Progress = "N/A";
                        }else{
                            String F_Progress =  String.valueOf(IssueData.getInt("F_Progress")); // 進度 5
                            Set_F_Progress = F_Progress;
                        }
                        Log.w("Set_F_Progress",Set_F_Progress);

                        if (IssueData.isNull("F_IsAudit")){
                            String F_IsAudit = "Null";
                            Set_F_IsAudit = false;
                        }else{
                            Boolean F_IsAudit = IssueData.getBoolean("F_IsAudit");//
                            Set_F_IsAudit = F_IsAudit;///注意是Boolen值
                        }
                        Log.w("Set_F_IsAudit",String.valueOf(Set_F_IsAudit));

                        if (IssueData.isNull("ModelLink")){
                            String ModelLink = "N/A";
                            Set_ModelLink = "N/A";
                        }else{
                            String ModelLink = IssueData.getString("ModelLink");
                            Set_ModelLink = ModelLink;
                        }
                        Log.w("Set_ModelLink",Set_ModelLink);

                        if (IssueData.isNull("IMSLink")){
                            String IMSLink = "N/A";
                            Set_IMSLink = "N/A";
                        }else{
                            String IMSLink = IssueData.getString("IMSLink");
                            Set_IMSLink = IMSLink;
                        }
                        Log.w("Set_IMSLink",Set_IMSLink);

                        if (IssueData.isNull("RSSLink")){
                            String RSSLink = "N/A";
                            Set_RSSLink = "N/A";
                        }else{
                            String RSSLink = IssueData.getString("RSSLink");
                            Set_RSSLink = RSSLink;
                        }
                        Log.w("Set_RSSLink",Set_RSSLink);

                        if (IssueData.isNull("Result")){
                            Boolean Result = false;
                            Set_Result = false;
                        }else{
                            Boolean Result = IssueData.getBoolean("Result");
                            Set_Result = Result;
                        }
                        Log.w("Set_Result",String.valueOf(Set_Result));

                        if (IssueData.isNull("Improve")){
                            String Improve = "N/A";
                            Set_Improve = "N/A";
                        }else{
                            String Improve = IssueData.getString("Improve");
                            Set_Improve = Improve;
                        }
                        Log.w("Set_Improve",Set_Improve);

                        //判斷PASS OR FAIL
                        if(Set_Result == true){
                            textView_Result.setEnabled(false);
                            textView_Result.setText("及格");
                            textView_Result.setTextColor(Color.parseColor("#3cd45b"));
                        }else{
                            textView_Result.setEnabled(true);
                            textView_Result.setText("不及格");
                            textView_Result.setTextColor(Color.parseColor("#ed4a47"));
                        }


                        textView_F_SeqNo.setText("No. "+Set_F_SeqNo);
                        textView_F_Model.setText(Set_F_Model);
                        textView_F_Subject.setText(Set_F_Subject);

                        textView_F_Author.setText(Set_F_Author);
                        textView_F_Progress.setText(Set_F_Progress+"%");

                        if(Set_F_TestResultID.indexOf("FAIL")==-1){//如果FAIL則變色 + 開放可點擊
                            if(Set_F_TestResultID.indexOf("PASS")==-1){
                                //其他
                                textView_F_TestResultID.setEnabled(false);
                                textView_F_TestResultID.setText(Set_F_TestResultID);
                                textView_F_TestResultID.setTextColor(Color.parseColor("#333333"));
                            }else {
                                //PASS
                                textView_F_TestResultID.setEnabled(false);
                                textView_F_TestResultID.setText(Set_F_TestResultID);
                                textView_F_TestResultID.setTextColor(Color.parseColor("#46aa36"));
                            }
                        }else{
                            //FAIL
                            textView_F_TestResultID.setEnabled(false);//暫時不給點
                            textView_F_TestResultID.setText(Set_F_TestResultID);
                            textView_F_TestResultID.setTextColor(Color.parseColor("#f0625d"));
                        }

                        textView_F_WorkHour.setText(Set_F_WorkHour);

                        if(Set_F_RegionID.indexOf("1") == -1){
                            textView_F_CostSum.setText(Set_F_CostSum + " RMB");
                        }else{
                            textView_F_CostSum.setText(Set_F_CostSum + " NTD");
                        }

                        if (IssueData.isNull("Lob")) {
                            String Lob = "N/A";
                            Set_Lob = "N/A";
                        } else {
                            String Lob = IssueData.getString("Lob");
                            Set_Lob = Lob;
                        }


                        textView_F_Topic.setText(Set_F_Topic);
                        textView_F_Stage.setText(Set_F_Stage);
                        textView_F_WType.setText(Set_F_WType);
                        textView_F_PeopleSum.setText(Set_F_PeopleSum);
                        textView_F_Ver.setText(Set_F_Ver);

                        textView_F_Result.setText(Set_F_Result);
                        textView_F_Env.setText(Set_F_Env);
                        textView_F_Comments.setText(Set_F_Comments);
                        textView_F_Platform.setText(Set_F_Platform);
                        textView_F_EQP.setText(Set_F_EQP);
                        textView_F_Ref.setText(Set_F_Ref);
                        textView_Lob.setText("("+Set_Lob+")");


                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();

                    }else{

                    }


                } catch (JSONException ex) {

                }

            }
        });

    }

    //查附件
    public void Find_RSS_File(String RSSNO) {

        DownloadFile_Data_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSS_File?RSSNO=" + RSSNO, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));


                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_FileName = String.valueOf(IssueData.getString("F_FileName")); //

                        String F_DownloadFilePath = String.valueOf(IssueData.getString("F_DownloadFilePath")); //

                        DownloadFile_Data_Item_List.add(i,new DownloadFile_Data_Item(F_FileName,F_DownloadFilePath));
                    }


                    mListView = (ListView)findViewById(R.id.lsv_F_Result);

                    mDownloadFile_Data_Adapter = new DownloadFile_Data_Adapter(mContext,DownloadFile_Data_Item_List);

                    mListView.setAdapter(mDownloadFile_Data_Adapter);



                } catch (JSONException ex) {

                }

            }
        });

    }

    public static void getString(String Url, RequestQueue mQueue, final GetServiceData.VolleyCallback callback) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("TestJsonObj");
                        System.out.println(error);
                    }
                }
        );

        int socketTimeout = 30000;
        getRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(getRequest);
    }

    public class DownloadFile_Data_Item  {

        String F_FileName;

        String F_DownloadFilePath;

        public DownloadFile_Data_Item(String F_FileName,String F_DownloadFilePath)
        {
            this.F_FileName = F_FileName;

            this.F_DownloadFilePath = F_DownloadFilePath;
        }

        public String GetF_FileName()
        {
            return this.F_FileName;
        }

        public String GetF_DownloadFilePath()
        {
            return this.F_DownloadFilePath;
        }

    }

    //------------------------------------Adapter-------------------------------------

    public class DownloadFile_Data_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<DownloadFile_Data_Item> DownloadFile_Data_Item_List;

        private Context Important_DataContext;

        private String Title;

        public DownloadFile_Data_Adapter(Context context, List<DownloadFile_Data_Item> DownloadFile_Data_Item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Important_DataContext = context;

            this.Title = Title;

            this.DownloadFile_Data_Item_List = DownloadFile_Data_Item_List;

        }

        @Override
        public int getCount() {
            return DownloadFile_Data_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return DownloadFile_Data_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(Important_DataContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_downloadfilepath_adapter, parent, false);

            TextView textView_F_DownloadFilePath = (TextView) v.findViewById(R.id.textView_F_DownloadFilePath);

            textView_F_DownloadFilePath.setText("附件連結:"+DownloadFile_Data_Item_List.get(position).GetF_FileName());

            textView_F_DownloadFilePath.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                    Uri uri=Uri.parse(DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath());
                    //Uri uri=Uri.parse(DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer"));
                    //Uri uri=Uri.parse(DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Get_File?FileName="));
                    Log.w("URL",String.valueOf(GetServiceData.ServicePath + "/Get_File?FileName=" + DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath()));
                    HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
                    Uri uri=Uri.parse(GetServiceData.ServicePath + "/Get_File?FileName=" + DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath());
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);

                    return false;
                }
            });

            return v;
        }
    }


}
