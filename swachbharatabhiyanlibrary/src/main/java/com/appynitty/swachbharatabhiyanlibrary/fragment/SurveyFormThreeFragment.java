package com.appynitty.swachbharatabhiyanlibrary.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.appynitty.swachbharatabhiyanlibrary.R;

public class SurveyFormThreeFragment extends Fragment {
    private Context context;
    private View view;
    private ImageView imgMinusAdult,imgPlusAdult;
    private ImageView imgMinusChild,imgPlusChild;
    private ImageView imgMinusSCitizen,imgPlusSCitizen;
    private ImageView imgMinusTwoWheel,imgPlusTwoWheel;
    private ImageView imgMinusFourWheel,imgPlusFourWheel;

    private TextView txtNumAdult;
    private TextView txtNumChild;
    private TextView txtNumSCitizen;
    private TextView txtNumTwoWheel;
    private TextView txtNumFourWheel;
    private TextView txtNumTotalVehicle;
    private TextView txtNumTotalMember;

    private String adult, children, SCitizen, totalMemberPlus, totalMemberMinus;
    private Integer i = 1;
    private Integer j = 1;
    private Integer k = 1;
    private Integer r = 1;

    private String twoWheel, fourWheel, totalVehiclePlus, totalVehicleMinus;
    private Integer l = 1;
    private Integer m = 1;
    private Integer n = 1;


    private CheckBox cbBusinessTypeYes, cbBusinessTypeNo;
    private CheckBox[] chkArrayBusinessType;
    private CheckBox cbLandA,cbShopA,cbOtherA;
    private CheckBox[] chkArrayAvailable;
    private CheckBox cbYesOCity,cbNoOCity;
    private CheckBox[] chkArrayOtherCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_survey_form_three, container, false);
            init();
        }
        return view;
    }

    private void init(){
        context = getActivity();

        //adult
        imgMinusAdult = view.findViewById(R.id.img_minus_adult);
        txtNumAdult = view.findViewById(R.id.txt_num_adult);
        imgPlusAdult = view.findViewById(R.id.img_plus_adult);
        //children
        imgMinusChild = view.findViewById(R.id.img_child_minus);
        txtNumChild = view.findViewById(R.id.txt_num_child);
        imgPlusChild = view.findViewById(R.id.img_child_plus);
        //Senior Citizen
        imgMinusSCitizen = view.findViewById(R.id.img_minus_citizen);
        txtNumSCitizen = view.findViewById(R.id.txt_num_citizen);
        imgPlusSCitizen = view.findViewById(R.id.img_plus_citizen);

        txtNumTotalMember = view.findViewById(R.id.txt_num_total_mem);

        //Two Wheeler
        imgMinusTwoWheel = view.findViewById(R.id.img_minus_two_wheeler);
        txtNumTwoWheel = view.findViewById(R.id.txt_num_two_wheeler);
        imgPlusTwoWheel = view.findViewById(R.id.img_plus_two_wheeler);

        txtNumTotalVehicle = view.findViewById(R.id.txt_num_total_vehicle);

        //Four Wheeler
        imgMinusFourWheel = view.findViewById(R.id.img_minus_four_wheeler);
        txtNumFourWheel = view.findViewById(R.id.txt_num_four_wheeler);
        imgPlusFourWheel = view.findViewById(R.id.img_plus_four_wheeler);


        //total family member
        i = Integer.parseInt(txtNumAdult.getText().toString());
        j = Integer.parseInt(txtNumChild.getText().toString());
        k = Integer.parseInt(txtNumSCitizen.getText().toString());
        r = Integer.parseInt(txtNumSCitizen.getText().toString());

        //total vehicle
        l = Integer.parseInt(txtNumTwoWheel.getText().toString());
        m = Integer.parseInt(txtNumFourWheel.getText().toString());
        n = Integer.parseInt(txtNumTotalVehicle.getText().toString());
//Business Type
        cbBusinessTypeYes = view.findViewById(R.id.cb_business_yes);
        cbBusinessTypeNo = view.findViewById(R.id.cb_business_no);

        chkArrayBusinessType = new CheckBox[2];
        chkArrayBusinessType[0] = cbBusinessTypeYes;
        chkArrayBusinessType[0].setOnClickListener(mListenerBusinessType);
        chkArrayBusinessType[1] = cbBusinessTypeNo;
        chkArrayBusinessType[1].setOnClickListener(mListenerBusinessType);
//Available
        cbLandA = view.findViewById(R.id.cb_land_available);
        cbShopA = view.findViewById(R.id.cb_shop_available);
        cbOtherA = view.findViewById(R.id.cb_other_available);

        chkArrayAvailable = new CheckBox[3];
        chkArrayAvailable[0] = cbLandA;
        chkArrayAvailable[0].setOnClickListener(mListenerAvailable);
        chkArrayAvailable[1] = cbShopA;
        chkArrayAvailable[1].setOnClickListener(mListenerAvailable);
        chkArrayAvailable[2] = cbOtherA;
        chkArrayAvailable[2].setOnClickListener(mListenerAvailable);
//other city
        cbYesOCity = view.findViewById(R.id.cb_yes_member_other_city);
        cbNoOCity = view.findViewById(R.id.cb_no_member_other_city);

        chkArrayOtherCity = new CheckBox[2];
        chkArrayOtherCity[0] = cbYesOCity;
        chkArrayOtherCity[0].setOnClickListener(mListenerOtherCity);
        chkArrayOtherCity[1] = cbNoOCity;
        chkArrayOtherCity[1].setOnClickListener(mListenerOtherCity);

        setOnClick();
    }

    private void setOnClick(){

        //adult
        imgMinusAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Decreasing value...");

                if (i > 0) {
                    i = i - 1;
                    adult = String.valueOf(i);
                    txtNumAdult.setText(adult);

                    r = i + j + k;
                    totalMemberMinus = String.valueOf(r);
                    txtNumTotalMember.setText( totalMemberMinus.replace("-","").trim());
                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });

        imgPlusAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Increasing value...");
                i = i + 1;
                adult = String.valueOf(i);
                txtNumAdult.setText(adult);

                r = i + j + k;
                totalMemberPlus = String.valueOf(r);
                txtNumTotalMember.setText( totalMemberPlus);
            }
        });
   //children
        imgMinusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Decreasing value...");

                if (j > 0) {
                    j = j - 1;
                    children = String.valueOf(j);
                    txtNumChild.setText(children);

                    r = i + j + k;
                    totalMemberMinus = String.valueOf(r);
                    txtNumTotalMember.setText( totalMemberMinus.replace("-","").trim());
                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });

        imgPlusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Increasing value...");
                j = j + 1;
                children = String.valueOf(j);
                txtNumChild.setText(children);

                r = i + j + k;
                totalMemberPlus = String.valueOf(r);
                txtNumTotalMember.setText( totalMemberPlus);
            }
        });
    //Senior Citizen
        imgMinusSCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Decreasing value...");

                if (k > 0) {
                    k = k - 1;
                    SCitizen = String.valueOf(k);
                    txtNumSCitizen.setText(SCitizen);

                    r = i + j + k;
                    totalMemberMinus = String.valueOf(r);
                    txtNumTotalMember.setText( totalMemberMinus.replace("-","").trim());
                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });

        imgPlusSCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Increasing value...");
                k = k + 1;
                SCitizen = String.valueOf(k);
                txtNumSCitizen.setText(SCitizen);

                r = i + j + k;
                totalMemberPlus = String.valueOf(r);
                txtNumTotalMember.setText( totalMemberPlus);
            }
        });

    //Two Wheeler
        imgMinusTwoWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Decreasing value...");

                if (l > 0) {
                    l = l - 1;
                    twoWheel = String.valueOf(l);
                    txtNumTwoWheel.setText(twoWheel);
                    txtNumTotalVehicle.setText(twoWheel);
                    n = l + m;
                    totalVehicleMinus = String.valueOf(n);
                    txtNumTotalVehicle.setText( totalVehicleMinus.replace("-","").trim());

                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });

        imgPlusTwoWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Increasing value...");
                l = l + 1;
                twoWheel = String.valueOf(l);
                txtNumTwoWheel.setText(twoWheel);

                n = m + l;
                totalVehiclePlus = String.valueOf(n);
                txtNumTotalVehicle.setText( totalVehiclePlus);
            }
        });

        //Four Wheeler
        imgMinusFourWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Decreasing value...");

                if (m > 0) {
                    m = m - 1;
                    fourWheel = String.valueOf(m);
                    txtNumFourWheel.setText(fourWheel);
                    txtNumTotalVehicle.setText(fourWheel);

                    n = m + l;
                    totalVehicleMinus = String.valueOf(n);
                    txtNumTotalVehicle.setText( totalVehicleMinus.replace("-","").trim());
                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });

        imgPlusFourWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Increasing value...");
                m = m + 1;
                fourWheel = String.valueOf(m);
                txtNumFourWheel.setText(fourWheel);

                n = m + l;
                totalVehiclePlus = String.valueOf(n);
                txtNumTotalVehicle.setText( totalVehiclePlus);
            }
        });

    }

    private View.OnClickListener mListenerBusinessType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayBusinessType.length; i++) {
                final CheckBox current = chkArrayBusinessType[i];
                if (current.getId() == checkedId) {
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                }
            }
        }
    };

    private View.OnClickListener mListenerAvailable = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayAvailable.length; i++) {
                final CheckBox current = chkArrayAvailable[i];
                if (current.getId() == checkedId) {
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                }
            }
        }
    };

    private View.OnClickListener mListenerOtherCity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayOtherCity.length; i++) {
                final CheckBox current = chkArrayOtherCity[i];
                if (current.getId() == checkedId) {
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                }
            }
        }
    };
}