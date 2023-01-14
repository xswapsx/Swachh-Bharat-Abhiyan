package com.appynitty.swachbharatabhiyanlibrary.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Locale;

public class SurveyFormFiveFragment extends Fragment {
    private static final String TAG = "SurveyFormFiveFragment";
    private Context context;
    private View view;
    private CheckBox cbLInsurance,cbMInsurance,cbBothInsurance;
    private CheckBox[] chkArrayInsurance;
    private CheckBox cbGov,cbPrivate;
    private CheckBox[] chkArrayIType;
    private CheckBox cbYesAyushman,cbNoAyushman;
    private CheckBox[] chkArrayAyushman;
    private CheckBox cbYesBooster, cbNoBooster;
    private CheckBox[] chkArrayBooster;
    private CheckBox cbYesDivang, cbNoDivang;
    private CheckBox[] chkArrayDivang;

    private String life,medical,both;
    String gov,pri;
    String yesAyu,noAyu;
    String yesBooster,noBooster;
    String yesDivyang,noDivyang;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_survey_form_five, container, false);
            init();
        }
        return view;
    }

    private void init() {
        context = getActivity();
//insurance
        cbLInsurance = view.findViewById(R.id.cb_life_insurance);
        life = getResStringLanguage(R.string.str_life_insurance,"en");
        cbMInsurance = view.findViewById(R.id.cb_medical_insurance);
        medical = getResStringLanguage(R.string.str_medical_insurance,"en");
        cbBothInsurance = view.findViewById(R.id.cb_both_insurance);
        both = getResStringLanguage(R.string.str_both,"en");

        chkArrayInsurance = new CheckBox[3];
        chkArrayInsurance[0] = cbLInsurance;
        chkArrayInsurance[0].setOnClickListener(mListenerInsurance);
        chkArrayInsurance[1] = cbMInsurance;
        chkArrayInsurance[1].setOnClickListener(mListenerInsurance);
        chkArrayInsurance[2] = cbBothInsurance;
        chkArrayInsurance[2].setOnClickListener(mListenerInsurance);

// Insurance Type
        cbGov = view.findViewById(R.id.cb_gov_insurance_type);
        gov = getResStringLanguage(R.string.str_government,"en");
        cbPrivate = view.findViewById(R.id.cb_private_insurance_type);
        pri = getResStringLanguage(R.string.str_private,"en");

        chkArrayIType = new CheckBox[2];
        chkArrayIType[0] = cbGov;
        chkArrayIType[0].setOnClickListener(mListenerIType);
        chkArrayIType[1] = cbPrivate;
        chkArrayIType[1].setOnClickListener(mListenerIType);
// Ayushman
        cbYesAyushman = view.findViewById(R.id.cb_yes_ayushman);
        yesAyu = getResStringLanguage(R.string.str_yes,"en");
        cbNoAyushman = view.findViewById(R.id.cb_no_ayushman);
        noAyu = getResStringLanguage(R.string.str_no,"en");

        chkArrayAyushman = new CheckBox[2];
        chkArrayAyushman[0] = cbYesAyushman;
        chkArrayAyushman[0].setOnClickListener(mListenerAyushman);
        chkArrayAyushman[1] = cbNoAyushman;
        chkArrayAyushman[1].setOnClickListener(mListenerAyushman);
//Booster Dose
        cbYesBooster = view.findViewById(R.id.cb_yes_booster);
        yesBooster = getResStringLanguage(R.string.str_yes,"en");
        cbNoBooster = view.findViewById(R.id.cb_no_booster);
        noBooster = getResStringLanguage(R.string.str_no,"en");

        chkArrayBooster = new CheckBox[2];
        chkArrayBooster[0] = cbYesBooster;
        chkArrayBooster[0].setOnClickListener(mListenerBooster);
        chkArrayBooster[1] = cbNoBooster;
        chkArrayBooster[1].setOnClickListener(mListenerBooster);
//Divang
        cbYesDivang = view.findViewById(R.id.cb_yes_divyang);
        yesDivyang = getResStringLanguage(R.string.str_yes,"en");
        cbNoDivang = view.findViewById(R.id.cb_no_divyang);
        noDivyang = getResStringLanguage(R.string.str_no,"en");

        chkArrayDivang = new CheckBox[2];
        chkArrayDivang[0] = cbYesDivang;
        chkArrayDivang[0].setOnClickListener(mListenerDivang);
        chkArrayDivang[1] = cbNoDivang;
        chkArrayDivang[1].setOnClickListener(mListenerDivang);




        setOnClick();
        setFillData();
    }

    private void setFillData() {
        String insurance = Prefs.getString(AUtils.PREFS.SUR_INSURANCE,"");
        if (insurance.equals(life)){
            cbLInsurance.setChecked(true);
        }else if (insurance.equals(medical)){
            cbMInsurance.setChecked(true);
        }else if (insurance.equals(both)){
            cbBothInsurance.setChecked(true);
        }
        String insuredType = Prefs.getString(AUtils.PREFS.SUR_UNDER_INSURANCE,"");
        if (insuredType.equals(String.valueOf(true))){
            cbGov.setChecked(true);
        }else if (insuredType.equals(String.valueOf(false))){
            cbPrivate.setChecked(true);
        }
        String ayushman = Prefs.getString(AUtils.PREFS.SUR_AYUSHMAN_BENE,"");
        if (ayushman.equals(String.valueOf(true))){
            cbYesAyushman.setChecked(true);
        }else if (ayushman.equals(String.valueOf(false))){
            cbNoAyushman.setChecked(true);
        }
        String booster = Prefs.getString(AUtils.PREFS.SUR_BOOSTER_SHOT,"");
        if (booster.equals(String.valueOf(true))){
            cbYesBooster.setChecked(true);
        }else if (booster.equals(String.valueOf(false))){
            cbNoBooster.setChecked(true);
        }
        String divyang = Prefs.getString(AUtils.PREFS.SUR_MEMBER_OF_DIVYANG,"");
        if (divyang.equals(String.valueOf(true))){
            cbYesDivang.setChecked(true);
        }else if (divyang.equals(String.valueOf(false))){
            cbNoDivang.setChecked(true);
        }
    }

    private void setOnClick() {

    }

    private View.OnClickListener mListenerInsurance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayInsurance.length; i++) {
                final CheckBox current = chkArrayInsurance[i];
                if (current.getId() == checkedId) {
                    CheckBox checkBoxInsurance = view.findViewById(current.getId());
                    String cbValueInsurance = checkBoxInsurance.getText().toString();
                    if (checkedId == R.id.cb_life_insurance){
                        cbValueInsurance = life;
                        Log.i("Social", "onClick: "+cbValueInsurance);
                        Prefs.putString(AUtils.PREFS.SUR_INSURANCE,cbValueInsurance);
                    }else if (checkedId == R.id.cb_medical_insurance){
                        cbValueInsurance = medical;
                        Log.i("Social", "onClick: "+cbValueInsurance);
                        Prefs.putString(AUtils.PREFS.SUR_INSURANCE,cbValueInsurance);
                    }else if (checkedId == R.id.cb_both_insurance){
                        cbValueInsurance = both;
                        Log.i("Social", "onClick: "+cbValueInsurance);
                        Prefs.putString(AUtils.PREFS.SUR_INSURANCE,cbValueInsurance);
                    }

                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                }
            }
        }
    };

    private View.OnClickListener mListenerIType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayIType.length; i++) {
                final CheckBox current = chkArrayIType[i];
                if (current.getId() == checkedId) {
                    CheckBox checkBoxIType = view.findViewById(current.getId());
                    String cbValueIType = checkBoxIType.getText().toString();
                    if (checkedId == R.id.cb_gov_insurance_type){
                        cbValueIType = gov;
                        Log.i("Social", "onClick: "+cbValueIType);
                        //  Prefs.putString(AUtils.PREFS.SUR_UNDER_INSURANCE,cbValueIType);
                    }else if (checkedId == R.id.cb_private_insurance_type){
                        cbValueIType = pri;
                        Log.i("Social", "onClick: "+cbValueIType);
                        //  Prefs.putString(AUtils.PREFS.SUR_UNDER_INSURANCE,cbValueIType);
                    }

                    Prefs.putString(AUtils.PREFS.SUR_UNDER_INSURANCE,String.valueOf(false));
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                    Prefs.putString(AUtils.PREFS.SUR_UNDER_INSURANCE,String.valueOf(true));
                }
            }
        }
    };

    private View.OnClickListener mListenerAyushman = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayAyushman.length; i++) {
                final CheckBox current = chkArrayAyushman[i];
                if (current.getId() == checkedId) {
                    CheckBox checkBoxAyushman = view.findViewById(current.getId());
                    String cbValueAyushman = checkBoxAyushman.getText().toString();
                    if (checkedId == R.id.cb_yes_ayushman){
                        cbValueAyushman = yesAyu;
                        Log.i("Social", "onClick: "+cbValueAyushman);
                        // Prefs.putString(AUtils.PREFS.SUR_AYUSHMAN_BENE,cbValueAyushman);
                    }else if (checkedId == R.id.cb_no_ayushman){
                        cbValueAyushman = noAyu;
                        Log.i("Social", "onClick: "+cbValueAyushman);
                        // Prefs.putString(AUtils.PREFS.SUR_AYUSHMAN_BENE,cbValueAyushman);
                    }
                    Prefs.putString(AUtils.PREFS.SUR_AYUSHMAN_BENE,String.valueOf(false));
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                    Prefs.putString(AUtils.PREFS.SUR_AYUSHMAN_BENE,String.valueOf(true));
                }
            }
        }
    };

    private View.OnClickListener mListenerBooster = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayBooster.length; i++) {
                final CheckBox current = chkArrayBooster[i];
                if (current.getId() == checkedId) {
                    CheckBox checkBoxBooster = view.findViewById(current.getId());
                    String cbValueBooster = checkBoxBooster.getText().toString();
                    if (checkedId == R.id.cb_yes_booster){
                        cbValueBooster = yesBooster;
                        Log.i("Social", "onClick: "+cbValueBooster);
                        //  Prefs.putString(AUtils.PREFS.SUR_BOOSTER_SHOT,cbValueBooster);
                    }else if (checkedId == R.id.cb_no_booster){
                        cbValueBooster = noBooster;
                        Log.i("Social", "onClick: "+cbValueBooster);
                        //  Prefs.putString(AUtils.PREFS.SUR_BOOSTER_SHOT,cbValueBooster);
                    }
                    Prefs.putString(AUtils.PREFS.SUR_BOOSTER_SHOT,String.valueOf(false));
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                    Prefs.putString(AUtils.PREFS.SUR_BOOSTER_SHOT,String.valueOf(true));
                }
            }
        }
    };

    private View.OnClickListener mListenerDivang = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            final String checkedValue = v.toString();
            for (int i = 0; i < chkArrayDivang.length; i++) {
                final CheckBox current = chkArrayDivang[i];
                if (current.getId() == checkedId) {
                    CheckBox checkBoxDivang = view.findViewById(current.getId());
                    String cbValueDivang = checkBoxDivang.getText().toString();
                    if (checkedId == R.id.cb_yes_divyang){
                        cbValueDivang =yesDivyang;
                        Log.i("Social", "onClick: "+cbValueDivang);
                    }else if (checkedId == R.id.cb_no_divyang){
                        cbValueDivang = noDivyang;
                        Log.i("Social", "onClick: "+cbValueDivang);
                    }

                    Prefs.putString(AUtils.PREFS.SUR_MEMBER_OF_DIVYANG, String.valueOf(false));
                    current.setChecked(true);
                } else {
                    current.setChecked(false);
                    Prefs.putString(AUtils.PREFS.SUR_MEMBER_OF_DIVYANG, String.valueOf(true));
                }
            }
        }
    };

    public String getResStringLanguage(int id, String lang){
        //Get default locale to back it
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        Locale savedLocale = conf.locale;
        //Retrieve resources from desired locale
        Configuration confAr = getResources().getConfiguration();
        confAr.locale = new Locale(lang);
        DisplayMetrics metrics = new DisplayMetrics();
        Resources resources = new Resources(getResources().getAssets(), metrics, confAr);
        //Get string which you want
        String string = resources.getString(id);
        //Restore default locale
        conf.locale = savedLocale;
        res.updateConfiguration(conf, null);
        //return the string that you want
        return string;
    }


    public static String getStringByLocal(Context context, int id, String lang) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(lang));
        return context.createConfigurationContext(configuration).getResources().getString(id);
    }

    private boolean isValid(){
        return true;
    }

    public Boolean checkFromFiveText(){
        if (isValid()){
            return true;
        }
        return false;
    }
}