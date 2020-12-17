package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_check_overtime_history_lastmonth.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_check_overtime_history_lastmonth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_check_overtime_history_lastmonth extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LayoutInflater minflater;

    private View mView;

    private Context mContext;

    private ProgressDialog progressBar;

    private static String GetDeptID;
    private static String GetWeek;

    private static String SetYear;

    private OnFragmentInteractionListener mListener;

    private LastmonthOverhourAdapter mLastmonthOverhourAdapter;
    private ListView mListView;
    private List<Lastmonth_Overhour_Item> Lastmonth_Overhour_Item_List = new ArrayList<Lastmonth_Overhour_Item>();

    private TextView lastmonth_total_number;
    private Double total_number;
    private TextView lastmonth_break_number;
    private Double break_number;
    private TextView lastmonth_money_number;
    private Double money_number;

    //抓年
    public void SetYear(String Year) {
        SetYear = Year;
    }

    //上月加班 個別
    public void Find_LastMonth_Over_Hour_Single(String DeptID, String Week, String Year, final String Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        break_number = 0.0;
        money_number = 0.0;

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_LastMonth_Over_Hour?DeptID="+DeptID+"&Week="+Week+"&Year="+Year +"&Type="+Type;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String WorkID = String.valueOf(IssueData.getString("WorkID"));

                        String CName = String.valueOf(IssueData.getString("CName"));

                        String Column1 = String.valueOf(IssueData.getDouble("Column1"));

                        if (Type.contains("1")){
                            break_number = break_number + Double.valueOf(Column1);
                        }else if(Type.contains("2")){
                            money_number = money_number + Double.valueOf(Column1);
                        }


                    }

                    lastmonth_break_number.setText(String.valueOf(break_number));
                    lastmonth_money_number.setText(String.valueOf(money_number));

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    //上月加班 總計
    public void Find_LastMonth_Over_Hour_Total(String DeptID,String Week, String Year,String Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        total_number = 0.0;
        break_number = 0.0;
        money_number = 0.0;

        Lastmonth_Overhour_Item_List.clear();


        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_LastMonth_Over_Hour?DeptID="+DeptID+"&Week="+Week+"&Year="+Year +"&Type="+Type;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String WorkID = String.valueOf(IssueData.getString("WorkID"));

                        String CName = String.valueOf(IssueData.getString("CName"));

                        String Column1 = String.valueOf(IssueData.getDouble("Column1"));
                        total_number = total_number + Double.valueOf(Column1);

                        Lastmonth_Overhour_Item_List.add(i,new Lastmonth_Overhour_Item(WorkID,CName,Column1));

                    }

                    lastmonth_total_number.setText(String.valueOf(total_number));

                    mListView = (ListView) mView.findViewById(R.id.listview_lastmonth);

                    mLastmonthOverhourAdapter = new LastmonthOverhourAdapter(mContext,Lastmonth_Overhour_Item_List);

                    mListView.setAdapter(mLastmonthOverhourAdapter);


                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }



    public msibook_dqaweekly_check_overtime_history_lastmonth() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Check_overtime_history_lastmonth.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_check_overtime_history_lastmonth newInstance(String param1, String param2,String Year,String DeptID,String Week) {
        msibook_dqaweekly_check_overtime_history_lastmonth fragment = new msibook_dqaweekly_check_overtime_history_lastmonth();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        SetYear = Year;
        GetDeptID = DeptID;
        GetWeek = Week;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        minflater = inflater;

        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_overtime_history_lastmonth, container, false);
        mContext = getContext();

        //讀取時間Bar
        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        lastmonth_total_number = (TextView) mView.findViewById(R.id.lastmonth_total_number);
        lastmonth_break_number = (TextView) mView.findViewById(R.id.lastmonth_break_number);
        lastmonth_money_number = (TextView) mView.findViewById(R.id.lastmonth_money_number);

        GetDeptID =  getActivity().getIntent().getStringExtra("Dpt_id");//抓部門ID
        GetWeek =  getActivity().getIntent().getStringExtra("Week");//抓週

        Find_LastMonth_Over_Hour_Total(GetDeptID,GetWeek,SetYear,"0");
        Find_LastMonth_Over_Hour_Single(GetDeptID,GetWeek,SetYear,"1");
        Find_LastMonth_Over_Hour_Single(GetDeptID,GetWeek,SetYear,"2");

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
        }
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //-------------------Item----------------

    public class Lastmonth_Overhour_Item {

        String WorkID;

        String CName;

        String Column1;

        public Lastmonth_Overhour_Item(String WorkID,String CName,String Column1)
        {
            this.WorkID = WorkID;

            this.CName = CName;

            this.WorkID = WorkID;

            this.Column1 = Column1;

        }


        public String GetWorkID()
        {
            return this.WorkID;
        }

        public String GetCName()
        {
            return this.CName;
        }

        public String GetColumn1()
        {
            return this.Column1;
        }

    }


    //-------------------Adapter-------------

    public class LastmonthOverhourAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Lastmonth_Overhour_Item> Lastmonth_Overhour_Item_List;

        private Context Lastmonth_Overhour_Context;


        public LastmonthOverhourAdapter(Context context,  List<Lastmonth_Overhour_Item> Lastmonth_Overhour_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Lastmonth_Overhour_Context = context;


            this.Lastmonth_Overhour_Item_List = Lastmonth_Overhour_Item_List;

        }
        @Override
        public int getCount() {
            return Lastmonth_Overhour_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Lastmonth_Overhour_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Lastmonth_Overhour_Context);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_lastmonth_overhour_adapter, parent, false);

            TextView adptextView1 = (TextView) v.findViewById(R.id.adptextView1);
            TextView adptextView2 = (TextView) v.findViewById(R.id.adptextView2);


            adptextView1.setText(Lastmonth_Overhour_Item_List.get(position).GetCName());

            adptextView2.setText(Lastmonth_Overhour_Item_List.get(position).GetColumn1());

            return v;
        }

    }


}