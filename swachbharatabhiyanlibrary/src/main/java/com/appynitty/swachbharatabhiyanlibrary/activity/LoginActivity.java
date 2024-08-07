package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.LoginAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.PopUpDialog;
import com.appynitty.swachbharatabhiyanlibrary.login.InternetWorking;
import com.appynitty.swachbharatabhiyanlibrary.login.viewmodel.LoginViewModel;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LanguagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.riaylibrary.utils.LocaleHelper;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Richali Pradhan Gupte on 24-10-2018.
 */
public class LoginActivity extends AppCompatActivity implements PopUpDialog.PopUpDialogListener {

    private static final String TAG = "LoginActivity";
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private Context mContext = null;

    private AutoCompleteTextView EtEmpType;
    private EditText txtUserName = null;
    private EditText txtUserPwd = null;

    private Button btnLogin = null;
    private Button btnChangeLang = null;

    private LoginPojo loginPojo = null;

    private LoginAdapterClass mAdapter;
    private LoginViewModel loginViewModel;

    String AppId = Prefs.getString(AUtils.APP_ID, "");

//    private PopupMenu popup;
    //spinner employee types
//    Spinner spnrEmpType;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (!allgranted) {

                Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{Manifest.permission
                                                    .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                }
                            }
                        }).show();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (AUtils.isInternetAvailable()) {
            AUtils.hideSnackBar();
        } else {
            AUtils.showSnackBar(findViewById(R.id.parent));
        }
    }

    @Override
    public void onPopUpDismissed(String type, Object listItemSelected, @Nullable String vehicleNo) {

        if (!AUtils.isNull(listItemSelected)) {
            if (AUtils.DIALOG_TYPE_LANGUAGE.equals(type)) {
                onLanguageTypeDialogClose(listItemSelected);
            }
        }
    }

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    protected void generateId() {

        getPermission();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mContext = LoginActivity.this;
        AUtils.currentContextConstant = mContext;

        mAdapter = new LoginAdapterClass();

        setContentView(R.layout.activity_login_layout);


        EtEmpType = findViewById(R.id.et_emp_type);
        /*EtEmpType.clearListSelection();
        EtEmpType.setText("");*/

        /*EtEmpType.setText(R.string.household_collection);*/
        EtEmpType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(LoginActivity.this, EtEmpType);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.emp_types_menu, popup.getMenu());


               /* // only nagpur appId 3068 menu
                popup.getMenuInflater()
                        .inflate(R.menu.emp_types_menu_nagpur, popup.getMenu());*/


//                popup.getMenu().getItem(2).setChecked(true);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        EtEmpType.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });


        txtUserName = findViewById(R.id.txt_user_name);
        txtUserPwd = findViewById(R.id.txt_password);

        btnLogin = findViewById(R.id.btn_login);
        btnChangeLang = findViewById(R.id.btn_change_lang);


    }

    protected void registerEvents() {

        /*EtEmpType.setOnClickListener(new View.OnClickListener() {     //Swapnil
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(LoginActivity.this, EtEmpType);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.emp_types_menu, popup.getMenu());

                popup.getMenu().getItem(2).setChecked(true);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        EtEmpType.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); */ //Swapnil

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.loginProgressBar).setVisibility(View.VISIBLE);

                if (AUtils.isInternetAvailable()) {

                    onLogin();
//                    if (AUtils.isConnectedFast(getApplicationContext())) {
//                        findViewById(R.id.loginProgressBar).setVisibility(View.VISIBLE);
//                        onLogin();
//
//                    } else {
//                        findViewById(R.id.loginProgressBar).setVisibility(View.INVISIBLE);
//                        AUtils.warning(LoginActivity.this, getResources().getString(R.string.slow_internet));
//                    }

                } else {
                    // findViewById(R.id.loginProgressBar).setVisibility(View.INVISIBLE);
                    findViewById(R.id.loginProgressBar).setVisibility(View.INVISIBLE);
                    AUtils.warning(LoginActivity.this, getResources().getString(R.string.no_internet_error));
                }

            }
        });

        btnChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });

        mAdapter.setLoginListener(new LoginAdapterClass.LoginListener() {
            @Override
            public void onSuccessCallBack() {
                String message;

                if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equalsIgnoreCase(AUtils.LanguageConstants.MARATHI)) {
                    message = mAdapter.getLoginDetailsPojo().getMessageMar();
                } else {
                    message = mAdapter.getLoginDetailsPojo().getMessage();
                }

                Prefs.putString(AUtils.PREFS.USER_ID, mAdapter.getLoginDetailsPojo().getUserId());
                Prefs.putString(AUtils.PREFS.USER_TYPE, mAdapter.getLoginDetailsPojo().getType());
                Prefs.putString(AUtils.PREFS.USER_TYPE_ID, mAdapter.getLoginDetailsPojo().getTypeId());
                Prefs.putString(AUtils.PREFS.EMPLOYEE_TYPE, mAdapter.getLoginDetailsPojo().getEmpType());
                Prefs.putBoolean(AUtils.PREFS.IS_GT_FEATURE, mAdapter.getLoginDetailsPojo().getGtFeatures());
                Log.e("LoginActivity", "empType- " + Prefs.getString(AUtils.PREFS.EMPLOYEE_TYPE, null));
                Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, true);

                AUtils.success(mContext, message, Toast.LENGTH_SHORT);

                Intent intent;
                String userType = mAdapter.getLoginDetailsPojo().getTypeId();
                intent = new Intent(LoginActivity.this, AUtils.getDashboardClass(userType));

                intent.putExtra(AUtils.isFromLogin, true);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onSuccessFailureCallBack() {
                String message;

                if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equalsIgnoreCase(AUtils.LanguageConstants.MARATHI)) {
                    message = mAdapter.getLoginDetailsPojo().getMessageMar();
                } else {
                    message = mAdapter.getLoginDetailsPojo().getMessage();
                    Log.e(TAG, "onSuccessFailureCallBack: " + message);
                }

                Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);

                AUtils.error(mContext, message, Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailureCallBack() {
                Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);
                AUtils.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT);

            }
        });
    }

    protected void initData() {

    }

    private void onLogin() {

        if (validateForm()) {
            getFormData();
//
//            final Executor executor = Executors.newSingleThreadExecutor();
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (InternetWorking.isOnline()) {
//
//                        Handler handler = new Handler(Looper.getMainLooper());
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {

            loginViewModel.loginUser(loginPojo);
            loginViewModel.getLoginDetailsSuccessLiveData().observe(LoginActivity.this, new Observer<LoginDetailsPojo>() {
                @Override
                public void onChanged(LoginDetailsPojo loginDetailsPojo) {

                    findViewById(R.id.loginProgressBar).setVisibility(View.INVISIBLE);
                    if (!AUtils.isNull(loginDetailsPojo) && !AUtils.isNull((loginDetailsPojo).getStatus())) {

                        if (loginDetailsPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                            Prefs.putString(AUtils.PREFS.USER_ID, loginDetailsPojo.getUserId());
                            Prefs.putString(AUtils.PREFS.USER_TYPE, loginDetailsPojo.getType());
                            Prefs.putString(AUtils.PREFS.USER_TYPE_ID, loginDetailsPojo.getTypeId());
                            Prefs.putString(AUtils.PREFS.EMPLOYEE_TYPE, loginDetailsPojo.getEmpType());
                            Prefs.putBoolean(AUtils.PREFS.IS_GT_FEATURE, loginDetailsPojo.getGtFeatures());
                            Log.e("LoginActivity", "empType- " + Prefs.getString(AUtils.PREFS.EMPLOYEE_TYPE, null));
                            Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, true);

                            Intent intent;
                            String userType = loginDetailsPojo.getTypeId();
                            intent = new Intent(LoginActivity.this, AUtils.getDashboardClass(userType));

                            intent.putExtra(AUtils.isFromLogin, true);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {

                            String message;

                            if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equalsIgnoreCase(AUtils.LanguageConstants.MARATHI)) {
                                message = loginDetailsPojo.getMessageMar();
                            } else {
                                message = loginDetailsPojo.getMessage();
                                Log.e(TAG, "onSuccessFailureCallBack: " + message);
                            }

                            Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);

                            AUtils.error(mContext, message, Toast.LENGTH_SHORT);
                        }

                    } else {

                        Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);
                        AUtils.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT);

                    }
                }
            });
            loginViewModel.getLoginDetailsErrorLiveData().observe(LoginActivity.this, new Observer<Throwable>() {
                @Override
                public void onChanged(Throwable throwable) {
                    AUtils.warning(LoginActivity.this, throwable.getMessage());

                    Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);
                    findViewById(R.id.loginProgressBar).setVisibility(View.GONE);

                }
            });

//                            }
//                        });
//
//                    } else {
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                findViewById(R.id.loginProgressBar).setVisibility(View.GONE);
//                                AUtils.warning(LoginActivity.this, getResources().getString(R.string.no_internet_error));
//                                //    noInternetErrorLayout.setVisibility(View.VISIBLE);
//                            }
//                        });
//
//                    }
//                }
//            });

        }

    }


    private boolean validateForm() {
        String strUsrName = txtUserName.getText().toString().trim();
        String strPwd = txtUserPwd.getText().toString().trim();

        if (EtEmpType.getText().toString().isEmpty()) {
            AUtils.warning(mContext, mContext.getString(R.string.plz_slct_emp_type));
            return false;
        }

        if (AUtils.isNullString(txtUserName.getText().toString())) {
            AUtils.warning(mContext, mContext.getString(R.string.plz_ent_username));
            return false;
        }

        if (AUtils.isNullString(txtUserPwd.getText().toString())) {
            AUtils.warning(mContext, mContext.getString(R.string.plz_ent_pwd));
            return false;
        }

        if (strUsrName.contains(" ") || strPwd.contains(" ")) {
            AUtils.warning(mContext, getString(R.string.usr_name_pwd_mismatch_warning));
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getFormData() {

        loginPojo = new LoginPojo();
        /*loginPojo.setMessage("");
        loginPojo.setStatus("");
        loginPojo.setType("");
        loginPojo.setUserId("");*/
        String empType = EtEmpType.getText().toString();
        String userName = txtUserName.getText().toString().replaceAll("\\s", "").trim();
        String password = txtUserPwd.getText().toString().replaceAll("\\s", "").trim();
        loginPojo.setUserLoginId(userName);
        loginPojo.setUserPassword(password);

        if (empType.matches(getResources().getString(R.string.household_collection))) {
            loginPojo.setEmployeeType("N");

        } else if (empType.matches(getResources().getString(R.string.street_sweeping))) {
            loginPojo.setEmployeeType("S");

        } else if (empType.matches(getResources().getString(R.string.liquid_waste_cleaning))) {
            loginPojo.setEmployeeType("L");

        } else if (empType.matches(getResources().getString(R.string.dump_yard_supervisor))) {
            loginPojo.setEmployeeType("D");

        }

//        Log.d("TAG", "Employee Type: " + loginPojo.getEmployeeType());
        Log.d("TAG", "getFormData: " + loginPojo.getUserLoginId());
        Log.d("TAG", "getFormData: " + loginPojo.getUserPassword());
        Log.d("LoginActivity", "Employee Type: " + loginPojo.getEmployeeType());

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId = AUtils.getAndroidId();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            deviceId = telephonyManager.getDeviceId();
        }

        loginPojo.setImiNo(deviceId);
    }

    private void getPermission() {

        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.CAMERA) +
                ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) +
                ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) +
//                ActivityCompat
//                .checkSelfPermission(LoginActivity.this,
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                ActivityCompat


                        .checkSelfPermission(LoginActivity.this,
                                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||


                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ||


//                    ActivityCompat.shouldShowRequestPermissionRationale
//                            (LoginActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ||


                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to start using application",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
//                                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                                    Manifest.permission.READ_PHONE_STATE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                }
                            }
                        }).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
//                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                    Manifest.permission.READ_PHONE_STATE},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        }
    }

    private void onLanguageTypeDialogClose(Object listItemSelected) {

        LanguagePojo languagePojo = (LanguagePojo) listItemSelected;
        changeLanguage(AUtils.setLanguage(languagePojo.getLanguage()));
        AUtils.info(mContext, "Selected language is : " + ((LanguagePojo) listItemSelected).getLanguage());
    }

    private void changeLanguage() {
        HashMap<Integer, Object> mLanguage = new HashMap<>();
        List<LanguagePojo> mLanguagePojoList = AUtils.getLanguagePojoList();

        AUtils.changeLanguage(this, Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID));

        if (!AUtils.isNull(mLanguagePojoList) && !mLanguagePojoList.isEmpty()) {
            for (int i = 0; i < mLanguagePojoList.size(); i++) {
                mLanguage.put(i, mLanguagePojoList.get(i));
            }

            PopUpDialog dialog = new PopUpDialog(LoginActivity.this, AUtils.DIALOG_TYPE_LANGUAGE, mLanguage, this);
            dialog.show();
        }
    }

    public void changeLanguage(String type) {

        AUtils.changeLanguage(this, type);
//        recreate();
//        generateId();


        Intent intent = getIntent();
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

    }


}
