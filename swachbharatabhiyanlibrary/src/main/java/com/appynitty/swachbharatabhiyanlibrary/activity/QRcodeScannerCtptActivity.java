package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.AutocompleteContainSearch;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.AreaHouseAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.AreaPointAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.CollectionAreaAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.DumpYardAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.GarbageCollectionAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.GarbageTypePopUp;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.ToiletTypePopUp;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaHousePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaPointPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionDumpYardPointPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OfflineGarbageColectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineAttendanceRepository;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineRepository;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyApplication;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;
import com.riaylibrary.custom_component.MyProgressDialog;
import com.riaylibrary.utils.LocaleHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/******  Rahul Rokade 24/01/22 **************/
public class QRcodeScannerCtptActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler, GarbageTypePopUp.GarbagePopUpDialogListener/*, ToiletTypePopUp.ToiletTypePopUpDialogListener*/ {

    private final static String TAG = "QRcodeScannerCtptActivity";
    private final static int DUMP_YARD_DETAILS_REQUEST_CODE = 100;
    GarbageCollectionPojo garbageCollectionPojo;
    private Context mContext;
    private Toolbar toolbar;
    private ZBarScannerView scannerView;
    private FabSpeedDial fabSpeedDial;
    private AutoCompleteTextView areaAutoComplete;
    private TextInputLayout idIpLayout, areaLayout;
    private AutoCompleteTextView idAutoComplete;
    private RadioGroup collectionRadioGroup;
    private RadioButton houseCollectionRadio, dumpYardRadio, commercialRadio;
    private String radioSelection;
    private Button submitBtn, permissionBtn;
    private View contentView;
    private boolean isActivityData;
    private ImagePojo imagePojo;
    private Boolean isScanQr;
    private HashMap<String, String> areaHash;
    private HashMap<String, String> idHash;
    private AreaHouseAdapterClass mHpAdapter;
    private AreaPointAdapterClass mGpAdapter;
    private DumpYardAdapterClass mDyAdapter;
    private CollectionAreaAdapterClass mAreaAdapter;
    private GarbageCollectionAdapterClass mAdapter;
    //        private SyncServerRepository syncServerRepository; //TODO
    private SyncOfflineRepository syncOfflineRepository;
    private SyncOfflineAttendanceRepository syncOfflineAttendanceRepository;

    private MyProgressDialog myProgressDialog;
    private ArrayList<Integer> mSelectedIndices;
    private ToiletTypePopUp toiletTypePopUp;
    TextView collectionStatus;
    private String EmpType, gcType;
    private String areaType;
    private Uri uri;
    private String CtptId;
    private String urlSting;


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
        if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_CAMERA) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                checkCameraPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA)) {

                AUtils.showPermissionDialog(mContext, "CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                });
            }
        } else if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_LOCATION) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                checkLocationPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {

                AUtils.showPermissionDialog(mContext, "Location Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }
                });
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPreview();

    }

    @Override
    protected void onPause() {
        stopPreview();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!isScanQr) {
            scanQR();
            setHints();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public void onGarbagePopUpDismissed(String houseID, int garbageType, @Nullable String comment) {
        if (garbageType != -1) {
            gcType = "10";
            validateTypeOfCollection(houseID);
            //   startSubmitQRAsyncTask(houseID, garbageType, gcType, comment);
        } else {
            restartPreview();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        AUtils.currentContextConstant = mContext;

        if (requestCode == DUMP_YARD_DETAILS_REQUEST_CODE && resultCode == RESULT_OK) {

            try {
                HashMap<String, String> map = (HashMap<String, String>) data.getSerializableExtra(AUtils.DUMPDATA.dumpDataMap);

                if (data.hasExtra(AUtils.REQUEST_CODE)) {
                    Type type = new TypeToken<ImagePojo>() {
                    }.getType();
                    imagePojo = new Gson().fromJson(Prefs.getString(AUtils.PREFS.IMAGE_POJO, null), type);

                    if (!AUtils.isNull(imagePojo)) {
                        isActivityData = true;
                    }
                }

                startSubmitQRAsyncTask(map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
//        setupFormats();
    }

    protected void generateId() {
        setContentView(R.layout.activity_qrcode_scanner_ctpt);
        toolbar = findViewById(R.id.toolbar);

        mContext = QRcodeScannerCtptActivity.this;
        AUtils.currentContextConstant = mContext;

        EmpType = Prefs.getString(AUtils.PREFS.EMPLOYEE_TYPE, null);
        collectionStatus = findViewById(R.id.collection_status);
        mAdapter = new GarbageCollectionAdapterClass();
        mDyAdapter = new DumpYardAdapterClass();
        mGpAdapter = new AreaPointAdapterClass();
        mHpAdapter = new AreaHouseAdapterClass();
        mAreaAdapter = new CollectionAreaAdapterClass();

        fabSpeedDial = findViewById(R.id.flash_toggle);

        areaLayout = findViewById(R.id.txt_area_layout);
        areaAutoComplete = findViewById(R.id.txt_area_auto);
        areaAutoComplete.setThreshold(0);
        areaAutoComplete.setDropDownBackgroundResource(R.color.white);
        areaAutoComplete.setSingleLine();


        idAutoComplete = findViewById(R.id.txt_id_auto);
        idAutoComplete.setThreshold(0);
        idAutoComplete.setDropDownBackgroundResource(R.color.white);
        idAutoComplete.setSingleLine();

        idIpLayout = findViewById(R.id.txt_id_layout);

        collectionRadioGroup = findViewById(R.id.collection_radio_group);
        houseCollectionRadio = findViewById(R.id.house_collection_radio);
        dumpYardRadio = findViewById(R.id.dump_yard_radio);
        commercialRadio = findViewById(R.id.comm_collection_radio);

        setHints();
        /***** Rahul Rokade ****/

        if (EmpType.matches("L")) {
            idAutoComplete.setHint(getResources().getString(R.string.lw_dy_id_hint));
            radioSelection = AUtils.RADIO_SELECTED_LW;
            houseCollectionRadio.setText(R.string.liquid_collection_radio);
            dumpYardRadio.setVisibility(View.GONE);
        } else if (EmpType.matches("S")) {
            idAutoComplete.setHint(getResources().getString(R.string.sw_dy_id_hint));
            radioSelection = AUtils.RADIO_SELECTED_SW;
            houseCollectionRadio.setText(R.string.street_collection_radio);
            dumpYardRadio.setVisibility(View.GONE);
        } else if (EmpType.matches("N")) {
            idAutoComplete.setHint(getResources().getString(R.string.hp_gp_id_hint));
            radioSelection = AUtils.RADIO_SELECTED_HP;
            houseCollectionRadio.setText(R.string.waste_collection_radio);
        } else {
            idAutoComplete.setHint("Enter CT/PT ID");
            radioSelection = AUtils.RADIO_SELECTED_CT;
            houseCollectionRadio.setText("Toilet Cleaning");
        }


        submitBtn = findViewById(R.id.submit_button);
        permissionBtn = findViewById(R.id.grant_permission);
        contentView = findViewById(R.id.scanner_view);

        imagePojo = null;
        isActivityData = false;
        isScanQr = true;

        ViewGroup contentFrame = findViewById(R.id.qr_scanner);
        scannerView = new ZBarScannerView(mContext);
        scannerView.setLaserColor(getResources().getColor(R.color.colorPrimary));
        scannerView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        contentFrame.addView(scannerView);
        areaAutoComplete.setVisibility(View.GONE);

        EmpType = Prefs.getString(AUtils.PREFS.EMPLOYEE_TYPE, null); //added by Swapnil
        gcType = "";

        initToolbar();

//        syncServerRepository = new SyncServerRepository(AUtils.mainApplicationConstant.getApplicationContext()); //TODO
        syncOfflineRepository = new SyncOfflineRepository(AUtils.mainApplicationConstant.getApplicationContext());
        syncOfflineAttendanceRepository = new SyncOfflineAttendanceRepository(AUtils.mainApplicationConstant.getApplicationContext());
    }

    private void setHints() {
        if (EmpType.matches("L")) {
            idAutoComplete.setHint(getResources().getString(R.string.lw_dy_id_hint));
            radioSelection = AUtils.RADIO_SELECTED_LW;
            houseCollectionRadio.setText(R.string.liquid_collection_radio);
            dumpYardRadio.setVisibility(View.GONE);
            commercialRadio.setVisibility(View.GONE);
        } else if (EmpType.matches("S")) {
            idAutoComplete.setHint(getResources().getString(R.string.sw_dy_id_hint));
            radioSelection = AUtils.RADIO_SELECTED_SW;
            houseCollectionRadio.setText(R.string.street_collection_radio);
            dumpYardRadio.setVisibility(View.GONE);
            commercialRadio.setVisibility(View.GONE);
        } else if (EmpType.matches("N")) {
            idIpLayout.setHint("");
            idAutoComplete.setHint(getResources().getString(R.string.hp_gp_id_hint));
            radioSelection = AUtils.RADIO_SELECTED_HP;
            houseCollectionRadio.setText(R.string.waste_collection_radio);
        } else if (EmpType.matches("CT")) {
            idAutoComplete.setHint("Enter toilet id");
            radioSelection = AUtils.RADIO_SELECTED_SW;
            houseCollectionRadio.setText("Toilet Cleaning");
            dumpYardRadio.setVisibility(View.GONE);
            commercialRadio.setVisibility(View.GONE);
        }
    }

    protected void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title_activity_qrcode_scanner));
        setSupportActionBar(toolbar);
        areaLayout.setVisibility(View.GONE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    protected void registerEvents() {
        Log.d(TAG, "registerEvents Area: " + areaAutoComplete.getText().toString());
        Log.d(TAG, "registerEvents Id : " + idAutoComplete.getText().toString());


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Boolean areaValid = isAutoCompleteValid(areaAutoComplete, areaHash);
                    Boolean idValid = isAutoCompleteValid(idAutoComplete, idHash);

                    if (areaValid && idValid) {
                        submitQRcode(idHash.get(idAutoComplete.getText().toString().toLowerCase()));

                    } else {
                        if (getAreaType().equals(AUtils.HP_AREA_TYPE_ID))
                            AUtils.error(mContext, mContext.getResources().getString(R.string.hp_area_validation));
                        else
                            AUtils.error(mContext, mContext.getResources().getString(R.string.gp_area_validation));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        collectionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioGroupId = radioGroup.getCheckedRadioButtonId();

                areaAutoComplete.setText("");
                idAutoComplete.setText("");
                AUtils.showKeyboard((Activity) mContext);


                /**** Rahul Rokade ******/
                //Liquid :-
                if (EmpType.matches("L")) {
                    idIpLayout.setHint(getResources().getString(R.string.liquid_number_hint));
                    radioSelection = AUtils.RADIO_SELECTED_HP;

                    if (!AUtils.isConnectedFast(mContext)) {
                        AUtils.warning(mContext, getResources().getString(R.string.slow_internet));
                    }
                    mAreaAdapter.fetchAreaList(getAreaType(), true);
                }

                //Street :-
                if (EmpType.matches("S")) {
                    idIpLayout.setHint(getResources().getString(R.string.street_number_hint));

                    if (!AUtils.isConnectedFast(mContext)) {
                        AUtils.warning(mContext, getResources().getString(R.string.slow_internet));
                    }
                    mAreaAdapter.fetchAreaList(getAreaType(), true);
                }

                //CT-PT
                if (EmpType.matches("CT")) {
                    idIpLayout.setHint(getResources().getString(R.string.street_number_hint));

                    if (!AUtils.isConnectedFast(mContext)) {
                        AUtils.warning(mContext, getResources().getString(R.string.slow_internet));
                    }
                    mAreaAdapter.fetchAreaList(getAreaType(), true);
                }

                //Waste :-

                if (EmpType.matches("N")) {
                    idIpLayout.setVisibility(View.GONE);
                    idIpLayout.setHint(getResources().getString(R.string.house_number_hint));

                    if (radioGroupId == R.id.house_collection_radio) {
                        idIpLayout.setHint(getResources().getString(R.string.house_number_hint));
                        radioSelection = AUtils.RADIO_SELECTED_HP;
                    } else if (radioGroupId == R.id.dump_yard_radio) {
                        idIpLayout.setHint(getResources().getString(R.string.dy_id_hint));
                        radioSelection = AUtils.RADIO_SELECTED_DY;
                    }

                    if (!AUtils.isConnectedFast(mContext)) {
                        AUtils.warning(mContext, getResources().getString(R.string.slow_internet));
                    }
                    mAreaAdapter.fetchAreaList(getAreaType(), true);
                }
            }
        });

        idAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused)
                    AUtils.showKeyboard((Activity) mContext);

                if (isFocused && isScanQr) {
                    Toast.makeText(mContext, "Focus changed!", Toast.LENGTH_SHORT).show();
                    hideQR();
                    AUtils.showKeyboard((Activity) mContext);
                } else {
                    idAutoComplete.clearListSelection();

                }

            }
        });

        idAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isAutoCompleteValid(idAutoComplete, idHash))
                    AUtils.hideKeyboard((Activity) mContext);
                else {
                    switch (getAreaType()) {
                        case AUtils.HP_AREA_TYPE_ID:
                            AUtils.error(mContext, mContext.getResources().getString(R.string.hp_validation));
                            break;
                        case AUtils.GP_AREA_TYPE_ID:
                            AUtils.error(mContext, mContext.getResources().getString(R.string.gp_validation));
                            break;
                        case AUtils.DY_AREA_TYPE_ID:
                            AUtils.error(mContext, mContext.getResources().getString(R.string.dy_validation));
                            break;
                    }
                }
            }
        });

        idAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (isAutoCompleteValid(idAutoComplete, idHash)) {
                        AUtils.hideKeyboard((Activity) mContext);
                        return false;
                    } else {
                        idAutoComplete.requestFocus();
                        switch (getAreaType()) {
                            case AUtils.HP_AREA_TYPE_ID:
                                AUtils.error(mContext, mContext.getResources().getString(R.string.hp_validation));
                                break;
                            case AUtils.GP_AREA_TYPE_ID:
                                AUtils.error(mContext, mContext.getResources().getString(R.string.gp_validation));
                                break;
                            case AUtils.DY_AREA_TYPE_ID:
                                AUtils.error(mContext, mContext.getResources().getString(R.string.dy_validation));
                                break;
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        areaAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (isAutoCompleteValid(areaAutoComplete, areaHash))
                    inflateAutoComplete(areaHash.get(areaAutoComplete.getText().toString().toLowerCase()));
                else
                    AUtils.error(mContext, mContext.getResources().getString(R.string.area_validation));
            }
        });

        areaAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (isAutoCompleteValid(areaAutoComplete, areaHash)) {
                        inflateAutoComplete(areaHash.get(areaAutoComplete.getText().toString().toLowerCase()));
                        return false;
                    } else {
                        areaAutoComplete.requestFocus();
                        AUtils.error(mContext, mContext.getResources().getString(R.string.area_validation));
                        return true;
                    }
                }

                return false;

            }
        });


        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

        fabSpeedDial.getMainFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scannerView.getFlash()) {
                    scannerView.setFlash(false);
                    fabSpeedDial.getMainFab().setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on_indicator));
                } else {
                    scannerView.setFlash(true);
                    fabSpeedDial.getMainFab().setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                }
            }
        });

        mAreaAdapter.setCollectionAreaListener(new CollectionAreaAdapterClass.CollectionAreaListener() {
            @Override
            public void onSuccessCallBack() {
                areaAutoComplete.clearListSelection();
                areaAutoComplete.requestFocus();
                idAutoComplete.clearListSelection();
                inflateAreaAutoComplete(mAreaAdapter.getAreaPojoList());

            }

            @Override
            public void onFailureCallBack() {
                AUtils.error(mContext, getResources().getString(R.string.serverError));

            }
        });

        mHpAdapter.setAreaHouseListener(new AreaHouseAdapterClass.AreaHouseListener() {
            @Override
            public void onSuccessCallBack() {
                inflateHpAutoComplete(mHpAdapter.getHpPojoList());
            }

            @Override
            public void onFailureCallBack() {
                AUtils.error(mContext, getResources().getString(R.string.serverError));
            }
        });

        mGpAdapter.setAreaPointListener(new AreaPointAdapterClass.AreaPointListener() {
            @Override
            public void onSuccessCallBack() {
                inflateGpAutoComplete(mGpAdapter.getGpPojoList());
            }

            @Override
            public void onFailureCallBack() {
                AUtils.error(mContext, getResources().getString(R.string.serverError));
            }
        });

        mDyAdapter.setAreaDyListener(new DumpYardAdapterClass.AreaDyListener() {
            @Override
            public void onSuccessCallBack() {
                inflateDyAutoComplete(mDyAdapter.getDyPojoList());
            }

            @Override
            public void onFailureCallBack() {
                AUtils.error(mContext, getResources().getString(R.string.serverError));
            }
        });

        mAdapter.setGarbageCollectionListener(new GarbageCollectionAdapterClass.GarbageCollectionListener() {
            @Override
            public void onSuccessCallBack() {

                if (mAdapter.getResultPojo().isAttendenceOff() && !syncOfflineAttendanceRepository.checkIsAttendanceIn()) {
                    AUtils.setIsOnduty(false);
                    ((MyApplication) AUtils.mainApplicationConstant).stopLocationTracking();
                    QRcodeScannerCtptActivity.this.finish();
                } else {

                    showPopup(getGarbageCollectionPojo().getId(), mAdapter.getResultPojo(), gcType);

                }
            }

            @Override
            public void onFailureCallBack(GarbageCollectionPojo garbageCollectionPojo) {
                Log.d(TAG, "onFailureCallBack: " + new Gson().toJson(garbageCollectionPojo));
                restartPreview();
                insertToDB(garbageCollectionPojo);
                AUtils.error(mContext, mContext.getString(R.string.serverError), Toast.LENGTH_SHORT);

            }
        });
    }

    protected void initData() {


        checkCameraPermission();

        if (!AUtils.isConnectedFast(mContext)) {
            AUtils.warning(mContext, getResources().getString(R.string.slow_internet));
        }

        mAreaAdapter.fetchAreaList(getAreaType(), false);

        Intent intent = getIntent();
        if (intent.hasExtra(AUtils.REQUEST_CODE)) {
            Type type = new TypeToken<ImagePojo>() {
            }.getType();
            imagePojo = new Gson().fromJson(Prefs.getString(AUtils.PREFS.IMAGE_POJO, null), type);

            if (!AUtils.isNull(imagePojo)) {
                isActivityData = true;
            }
        }
    }

    private void submitQRcode(String houseid) {

        //Prefs.getString(AUtils.PREFS.EMPLOYEE_TYPE, null
        if (EmpType.matches("L")) {

            //  gcType = "4";
            gcType = "10";

            if (houseid.substring(0, 2).matches("^[LlWw]+$")) {
                startSubmitQRAsyncTask(houseid, -1, gcType, null, null, null);
            } else if (houseid.substring(0, 2).matches("^[HhPp]+$")) {
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.house_qr_alert), null);
            } else if (houseid.substring(0, 2).matches("^[GgPp]+$")) {
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.gp_qr_alert), null);
            } else if (houseid.substring(0, 2).matches("^[DdYy]+$")) {
//                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.dy_qr_alert), null);
                getDumpYardDetails(houseid);
            }
        } else if (EmpType.matches("S")) {

            // gcType = "5";
            gcType = "10";

            if (houseid.substring(0, 2).matches("^[SsSs]+$")) {
                startSubmitQRAsyncTask(houseid, -1, gcType, null, null, null);
            } else if (houseid.substring(0, 2).matches("^[HhPp]+$")) {
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.house_qr_alert), null);
            } else if (houseid.substring(0, 2).matches("^[GgPp]+$")) {
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.gp_qr_alert), null);
            } else if (houseid.substring(0, 2).matches("^[DdYy]+$")) {
//                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.dy_qr_alert), null);
                getDumpYardDetails(houseid);
            }
        } else if (EmpType.matches("N")) {
            gcType = "10";
           // toiletTypePopUp = new ToiletTypePopUp(mContext, houseid, this);
            if (houseid.substring(0, 2).matches("^[CcTtPpTt]+$")) {
                // toiletTypePopUp.show();
                startSubmitQRAsyncTask(houseid, -1, gcType, null, imagePojo.getTNS(), null);
                // startSubmitQRAsyncTask(houseid, -1, gcType, null,0, null);
            } else if (houseid.substring(0, 3).matches("^[TtMmCc]+$")) {
                startSubmitQRAsyncTask(houseid, -1, gcType, null, imagePojo.getTNS(), null);
            } else if (houseid.substring(0, 2).matches("^[DdYy]+$")) {
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.dy_qr_alert), null);
            } else if (houseid.substring(0, 2).matches("^[LlWw]+$")) {
//                AUtils.warning(QRcodeScannerActivity.this, "For scanning Liquid Waste Collection QR,\nkindly login with liquid waste cleaning id", 16);
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.lwc_qr_alert), null);
            } else if (houseid.substring(0, 2).matches("^[SsSs]+$")) {
//                AUtils.warning(QRcodeScannerActivity.this, "For scanning Liquid Waste Collection QR,\nkindly login with liquid waste cleaning id", 16);
                AUtils.showDialog(mContext, getResources().getString(R.string.alert), getResources().getString(R.string.ssc_qr_warning), null);
            } else {
                AUtils.warning(QRcodeScannerCtptActivity.this, mContext.getResources().getString(R.string.qr_error));
                restartPreview();
            }
        } else if (EmpType.matches("CT")) {
            gcType = "10";
           // toiletTypePopUp = new ToiletTypePopUp(mContext, CtptId, this);

            uri = Uri.parse(houseid);
            CtptId = uri.getQueryParameter("sid");
            Log.e(TAG,"CTPT id is: "+CtptId);

            startSubmitQRAsyncTask(/*houseid*/ CtptId , 0, gcType, null, imagePojo.getTNS(), null);


            /*if (houseid.substring(0, 2).matches("^[CcTtPpTt]+$")) {
                // toiletTypePopUp.show();
                startSubmitQRAsyncTask(houseid, 0, gcType, null, imagePojo.getTNS(), null);
                // startSubmitQRAsyncTask(houseid, -1, gcType, null,0, null);
            } else if (houseid.substring(0, 3).matches("^[TtMmCc]+$")) {
                startSubmitQRAsyncTask(houseid, -1, gcType, null, imagePojo.getTNS(), null);
            }*/
        }

    }


    private void showPopup(String id, GcResultPojo pojo, String gcType) {


        Log.d(TAG, "showPopup: " + new Gson().toJson(pojo));
        Log.d(TAG, "showPopup: " + id);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.layout_qr_result, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        if (!dialog.isShowing()) {

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }

            dialog.show();

        }

        final String responseStatus = pojo.getStatus();
        TextView ownerName = view.findViewById(R.id.house_owner_name);
        TextView ownerMobile = view.findViewById(R.id.house_owner_mobile);
        TextView houseId = view.findViewById(R.id.house_id);

        ImageView statusImage = view.findViewById(R.id.response_image);
        Button doneBtn = view.findViewById(R.id.done_btn);

        if (responseStatus.equals(AUtils.STATUS_ERROR)) {
            statusImage.setImageDrawable(getDrawable(R.drawable.ic_cancel_red));
            doneBtn.setText(getString(R.string.retry_txt));
            houseId.setText(null);
            if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equals("2")) {
                collectionStatus.setText(pojo.getMessageMar());
            } else {
                collectionStatus.setText(pojo.getMessage());
            }
            ownerName.setText(id.toUpperCase());
            ownerMobile.setText(null);
        } else if (responseStatus.equals(AUtils.STATUS_SUCCESS)) {
            if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equals("2")) {
                ownerName.setText(pojo.getNameMar());
            } else {
                ownerName.setText(pojo.getName());
            }

            if (id.substring(0, 2).matches("^[HhPp]+$")) {
                ownerMobile.setText(pojo.getMobile());
            } else if (id.substring(0, 2).matches("^[CcTtPpTt]+$")) {
                ownerMobile.setText(pojo.getMobile());
            } else if (id.substring(0, 2).matches("^[GgPp]+$")) {
                ownerMobile.setVisibility(View.GONE);
            } else if (id.substring(0, 2).matches("^[DdYy]+$")) {
                ownerMobile.setVisibility(View.GONE);
                collectionStatus.setText(getResources().getString(R.string.garbage_deposit_completed));
            } else if (id.substring(0, 3).matches("^[TtMmCc]+$")) {
                ownerMobile.setText(pojo.getMobile());
            }

            houseId.setText(id);
//            ownerName.setText(pojo.getName());
//            ownerMobile.setText(pojo.getMobile());

            Type type = new TypeToken<ImagePojo>() {
            }.getType();

            imagePojo = new Gson().fromJson(
                    Prefs.getString(AUtils.PREFS.IMAGE_POJO, null), type);
        }

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                restartPreview();
                if (responseStatus.equals(AUtils.STATUS_SUCCESS)) {
                    imagePojo = null;
                    Prefs.putString(AUtils.PREFS.IMAGE_POJO, null);
                    finish();
                }
            }
        });

        String valueOne = "";
        switch (gcType) {
            case "1":
                valueOne = "6";
                break;
            case "2":
                valueOne = "7";
                break;
        }
        gcType.equals(valueOne);

    }

    private void checkCameraPermission() {
        if (AUtils.isCameraPermissionGiven(mContext)) {
            startPreview();
            contentView.setVisibility(View.VISIBLE);
            permissionBtn.setVisibility(View.GONE);
            checkLocationPermission();
        } else {
            contentView.setVisibility(View.GONE);
            permissionBtn.setVisibility(View.VISIBLE);
        }
    }

    private void scanQR() {
        isScanQr = true;
        startCamera();
        contentView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);
        collectionRadioGroup.setVisibility(View.GONE);
        areaLayout.setVisibility(View.GONE);
        areaAutoComplete.setVisibility(View.GONE);
        areaAutoComplete.setText("");
        idAutoComplete.clearFocus();
        idAutoComplete.setText("");
//        idIpLayout.setHint(getResources().getString(R.string.hp_gp_id_hint));
        scannerView.setAutoFocus(true);


    }

    private void hideQR() {
        isScanQr = false;
        stopCamera();
        contentView.setVisibility(View.GONE);
        submitBtn.setVisibility(View.VISIBLE);
        collectionRadioGroup.setVisibility(View.VISIBLE);
        areaLayout.setVisibility(View.VISIBLE);
        areaAutoComplete.setVisibility(View.VISIBLE);
        areaAutoComplete.requestFocusFromTouch();
        areaAutoComplete.setSelected(true);

        if (radioSelection.equals(AUtils.RADIO_SELECTED_HP)) {
            idIpLayout.setHint(getResources().getString(R.string.house_number_hint));
            idAutoComplete.setHint("");
        } else if (radioSelection.equals(AUtils.RADIO_SELECTED_LW)) {
            idIpLayout.setHint(getResources().getString(R.string.liquid_number_hint));
            idAutoComplete.setHint("");
        } else if (radioSelection.equals(AUtils.RADIO_SELECTED_SW)) {
            idIpLayout.setHint(getResources().getString(R.string.street_number_hint));
            idAutoComplete.setHint("");
        } else if (radioSelection.equals(AUtils.RADIO_SELECTED_CT)) {
//            Toast.makeText(mContext, "Toilet collection selected!", Toast.LENGTH_SHORT).show();
            idIpLayout.setHint("Enter CT/PT ID");
            idAutoComplete.setHint("");
        } else if (radioSelection.equals(AUtils.RADIO_SELECTED_CP)) {
//            Toast.makeText(mContext, "Toilet collection selected!", Toast.LENGTH_SHORT).show();
            idIpLayout.setHint(getResources().getString(R.string.commercial_id_hint));
            idAutoComplete.setHint("");
        }
        /*else {
            idIpLayout.setHint(getResources().getString(R.string.gp_id_hint));
        }*/

    }

    @SuppressLint("MissingPermission")
    private void checkLocationPermission() {

        if (AUtils.isLocationPermissionGiven(mContext)) {
            //You already have the permission, just go ahead.
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!GpsStatus) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }//else{
            // AUtils.saveLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            // }
        }
    }

    public void handleResult(Result result) {

        Log.d(TAG, "handleResult: " + new Gson().toJson(result));
        uri = Uri.parse(String.valueOf(result));
        CtptId = uri.getQueryParameter("sid");
        Log.e(TAG,"CTPT id is: "+CtptId);

        submitQRcode(result.getContents());
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

//        restartPreview();
    }

    private void startPreview() {
//        areaAutoComplete.setVisibility(View.GONE);
        scannerView.startCamera();
        scannerView.resumeCameraPreview(this);
    }

    private void stopPreview() {
        scannerView.stopCameraPreview();
        scannerView.stopCamera();
    }

    private void startCamera() {
        scannerView.startCamera();
    }

    private void stopCamera() {
        scannerView.stopCamera();
    }

    private void restartPreview() {

        stopPreview();
        startPreview();
    }

    private void validateTypeOfCollection(String houseid) {
        if (Prefs.getBoolean(AUtils.PREFS.IS_GT_FEATURE, false)) {
            ToiletTypePopUp dialog = new ToiletTypePopUp(QRcodeScannerCtptActivity.this, houseid, (ToiletTypePopUp.ToiletTypePopUpDialogListener) this);
            dialog.show();
        } else {
            gcType = "10";
            startSubmitQRAsyncTask(houseid, 0, gcType, null, imagePojo.getTNS(), null);
        }
    }

    private void startSubmitQRAsyncTask(final String houseNo, @Nullable final int garbageType, @Nullable final String gcType, @Nullable final String comment, final String tns, final String toiletType) {

        stopCamera();
        setGarbageCollectionPojo(houseNo, garbageType, gcType, comment, tns, toiletType);

        Log.d(TAG, "startSubmitQRAsyncTask: " + new Gson().toJson(garbageCollectionPojo));
        insertToDB(garbageCollectionPojo);

    }

    private void startSubmitQRAsyncTask(HashMap<String, String> map) {

        stopCamera();
        setGarbageCollectionPojo(map);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

//        if(AUtils.isInternetAvailable() && AUtils.isConnectedFast(mContext)) {
//            mAdapter.submitQR(garbageCollectionPojo);
//        } else {
        insertToDB(garbageCollectionPojo);
//        }
    }

    private void getDumpYardDetails(final String houseNo) {

        Intent intent = new Intent(mContext, DumpYardWeightActivity.class);
        intent.putExtra(AUtils.dumpYardId, houseNo);
        startActivityForResult(intent, DUMP_YARD_DETAILS_REQUEST_CODE);
    }

    private void inflateAreaAutoComplete(List<CollectionAreaPojo> pojoList) {

        areaHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for (CollectionAreaPojo pojo : pojoList) {
            areaHash.put(pojo.getArea().toLowerCase()/**/, pojo.getId());
            keyList.add(pojo.getArea().trim());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        areaAutoComplete.setThreshold(0);
        areaAutoComplete.setAdapter(adapter);
        if (!areaAutoComplete.isFocused()) {
            areaAutoComplete.requestFocus();
        }
//        areaAutoComplete.showDropDown();

    }

    private void inflateHpAutoComplete(List<CollectionAreaHousePojo> pojoList) {

        idHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for (CollectionAreaHousePojo pojo : pojoList) {
            idHash.put(pojo.getHouseNumber().toLowerCase(), pojo.getHouseid());
            keyList.add(pojo.getHouseNumber().trim());
        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        AutocompleteContainSearch adapter = new AutocompleteContainSearch(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        idAutoComplete.setThreshold(0);
        idAutoComplete.setAdapter(adapter);
        idAutoComplete.requestFocus();
    }

    private void inflateGpAutoComplete(List<CollectionAreaPointPojo> pojoList) {

        idHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for (CollectionAreaPointPojo pojo : pojoList) {
            idHash.put(pojo.getGpName().toLowerCase(), pojo.getGpId());
            keyList.add(pojo.getGpName().trim());
        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        AutocompleteContainSearch adapter = new AutocompleteContainSearch(mContext, android.R.layout.simple_dropdown_item_1line, keyList);

        idAutoComplete.setThreshold(0);
        idAutoComplete.setAdapter(adapter);
        idAutoComplete.requestFocus();
    }

    private void inflateDyAutoComplete(List<CollectionDumpYardPointPojo> pojoList) {

        idHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for (CollectionDumpYardPointPojo pojo : pojoList) {
            idHash.put(pojo.getDyId().toLowerCase(), pojo.getDyId());
            keyList.add(pojo.getDyId().trim());
        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        AutocompleteContainSearch adapter = new AutocompleteContainSearch(mContext, android.R.layout.simple_dropdown_item_1line, keyList);

        idAutoComplete.setThreshold(0);
        idAutoComplete.setAdapter(adapter);
        idAutoComplete.requestFocus();
    }

    private void inflateAutoComplete(String areaId) {
        //rahul
        String areaType = getAreaType();
        /*if (areaType.equals(AUtils.HP_AREA_TYPE_ID)) {
            mHpAdapter.fetchHpList(areaId);
        }*/

        if (areaType.equals(AUtils.GP_AREA_TYPE_ID)) {
            mGpAdapter.fetchGpList(areaId);
        }

        if (areaType.equals(AUtils.DY_AREA_TYPE_ID)) {
            mDyAdapter.fetchDyList(areaId);
        }

        if (areaType.equals(AUtils.CT_AREA_TYPE_ID)) {
            Toast.makeText(mContext, "fetch CTPT List!", Toast.LENGTH_SHORT).show();
        }
    }


    private String getAreaType() {
        areaType = AUtils.HP_AREA_TYPE_ID;

        if (radioSelection.equals(AUtils.RADIO_SELECTED_HP)) {
            areaType = AUtils.HP_AREA_TYPE_ID;
        } else if (radioSelection.equals(AUtils.RADIO_SELECTED_DY)) {
            areaType = AUtils.DY_AREA_TYPE_ID;
        }
        return areaType;
    }

    private Boolean isAutoCompleteValid(AutoCompleteTextView autoCompleteTextView, HashMap<String, String> hashMap) {
        try {
            return hashMap.containsKey(autoCompleteTextView.getText().toString().toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setGarbageCollectionPojo(String houseNo, @Nullable final int garbageType, final String gcType, @Nullable final String comment, final String tns, final String toiletType) {
        garbageCollectionPojo = new GarbageCollectionPojo();
        garbageCollectionPojo.setId(houseNo);
        garbageCollectionPojo.setGarbageType(garbageType);
        garbageCollectionPojo.setComment(comment);
        //garbageCollectionPojo.setTNS(tns);
        garbageCollectionPojo.setTNS("2");
        garbageCollectionPojo.setGcType(gcType);
        //garbageCollectionPojo.setTOT(toiletType);
        garbageCollectionPojo.setTOT("CT");
        double newlat = Double.parseDouble(Prefs.getString(AUtils.LAT, "0"));
        double newlng = Double.parseDouble(Prefs.getString(AUtils.LONG, "0"));
        garbageCollectionPojo.setDistance(AUtils.calculateDistance(mContext, newlat, newlng));
        if (isActivityData) {
            garbageCollectionPojo.setAfterImage(imagePojo.getAfterImage());
            garbageCollectionPojo.setBeforeImage(imagePojo.getBeforeImage());
            garbageCollectionPojo.setComment(imagePojo.getComment());
            garbageCollectionPojo.setImage1(imagePojo.getImage1());
            garbageCollectionPojo.setImage2(imagePojo.getImage2());
            garbageCollectionPojo.setGpBeforImageTime(Prefs.getString(AUtils.BEFORE_IMAGE_TIME,""));
           // garbageCollectionPojo.setTNS(imagePojo.getTNS());
            garbageCollectionPojo.setTNS("2");
        }


    }

    private GarbageCollectionPojo getGarbageCollectionPojo() {
        Log.d(TAG, "getGarbageCollectionPojo: " + new Gson().toJson(garbageCollectionPojo));
        return garbageCollectionPojo;
    }

    private void setGarbageCollectionPojo(HashMap<String, String> map) {
        try {
            garbageCollectionPojo = new GarbageCollectionPojo();
            garbageCollectionPojo.setId(map.get(AUtils.DUMPDATA.dumpYardId));
            garbageCollectionPojo.setGarbageType(-1);
            garbageCollectionPojo.setComment(null);
            double newlat = Double.parseDouble(Prefs.getString(AUtils.LAT, "0"));
            double newlng = Double.parseDouble(Prefs.getString(AUtils.LONG, "0"));
            garbageCollectionPojo.setDistance(AUtils.calculateDistance(mContext, newlat, newlng));
            if (isActivityData) {
                garbageCollectionPojo.setAfterImage(imagePojo.getAfterImage());
                garbageCollectionPojo.setBeforeImage(imagePojo.getBeforeImage());
                garbageCollectionPojo.setComment(imagePojo.getComment());
                garbageCollectionPojo.setImage1(imagePojo.getImage1());
                garbageCollectionPojo.setImage2(imagePojo.getImage2());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertToDB(GarbageCollectionPojo garbageCollectionPojo) {

        OfflineGarbageColectionPojo entity = new OfflineGarbageColectionPojo();

        entity.setReferenceID(garbageCollectionPojo.getId());
        if (garbageCollectionPojo.getId().substring(0, 2).matches("^[CcTtPpTt]+$")) {
            if ((gcType.equalsIgnoreCase("10")) && gcType.matches("10")) {
                getIntent().getStringArrayExtra("CTPT");
                entity.setGcType(String.valueOf(AUtils.CTPT_GC_TYPE));
            }
        } else if (garbageCollectionPojo.getId().substring(0, 3).matches("^[TtMmCc]+$")) {
            if ((gcType.equalsIgnoreCase("10")) && gcType.matches("10")) {
                getIntent().getStringArrayExtra("CTPT");
                entity.setGcType(String.valueOf(AUtils.CTPT_GC_TYPE));
            }
        }
        entity.setNote(garbageCollectionPojo.getComment());
        entity.setTNS(garbageCollectionPojo.getTNS());
        entity.setTOT(garbageCollectionPojo.getTOT());
        entity.setGarbageType(String.valueOf(garbageCollectionPojo.getGarbageType()));
        entity.setTotalGcWeight(String.valueOf(garbageCollectionPojo.getWeightTotal()));
        entity.setTotalDryWeight(String.valueOf(garbageCollectionPojo.getWeightTotalDry()));
        entity.setTotalWetWeight(String.valueOf(garbageCollectionPojo.getWeightTotalWet()));
        //entity.setVehicleNumber(Prefs.getString(AUtils.VEHICLE_NO, ""));
        entity.setVehicleNumber("1");
        entity.setLong(Prefs.getString(AUtils.LONG, ""));
        entity.setLat(Prefs.getString(AUtils.LAT, ""));
//        entity.setGcDate(AUtils.getServerDateTime());
        entity.setGcDate(AUtils.getServerDateTimeLocal());
        entity.setDistance(String.valueOf(garbageCollectionPojo.getDistance()));

        entity.setIsOffline(AUtils.isInternetAvailable() && AUtils.isConnectedFast(mContext));

        if (isActivityData) {
//            entity.setAfterImage(imagePojo.getAfterImage());
//            entity.setBeforeImage(imagePojo.getBeforeImage());
//            entity.setComment(imagePojo.getComment());

            try {
                if (imagePojo.getImage1() != null && imagePojo.getImage2() != null) {
                    entity.setGpBeforImage(AUtils.getEncodedImage(imagePojo.getImage1(), this));
                    entity.setGpAfterImage(AUtils.getEncodedImage(imagePojo.getImage2(), this));
                    entity.setGpBeforImageTime("11:11:55 am");
                    entity.setGpBeforImageTime(Prefs.getString(AUtils.BEFORE_IMAGE_TIME, null));
                    Log.e(TAG, "Images are there!");
                } else if (!Prefs.getString(AUtils.BEFORE_IMAGE, null).isEmpty() || !AUtils.isNullString(Prefs.getString(AUtils.BEFORE_IMAGE, null))) {
                    entity.setGpBeforImage(AUtils.getEncodedImage(Prefs.getString(AUtils.BEFORE_IMAGE, null), this));
                    entity.setGpAfterImage(AUtils.getEncodedImage(Prefs.getString(AUtils.AFTER_IMAGE, null), this));
                    entity.setGpBeforImageTime(Prefs.getString(AUtils.BEFORE_IMAGE_TIME, null));
                } else {
                    entity.setGpBeforImage("");
                    entity.setGpAfterImage("");
                    Log.e(TAG, "Images are null!");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        SyncServerRepository   mSyncServerRepository = new SyncServerRepository(AUtils.mainApplicationConstant.getApplicationContext());
//
//
//        Type type = new TypeToken<OfflineGarbageColectionPojo>() {}.getType(); //TODO
//        mSyncServerRepository.insertSyncServerEntity(new Gson().toJson(entity, type)); //TODO

        syncOfflineRepository.insertCollection(entity);
        Prefs.remove(AUtils.BEFORE_IMAGE);
        Prefs.remove(AUtils.AFTER_IMAGE);
        Prefs.remove(AUtils.BEFORE_IMAGE_TIME);
        showOfflinePopup(garbageCollectionPojo.getId(), entity.getGcType());
    }

    private void showOfflinePopup(String pojo, String garbageType) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.layout_qr_result, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        if (!dialog.isShowing()) {
            dialog.show();

        }

        TextView ownerName = view.findViewById(R.id.house_owner_name);
        TextView houseTitle = view.findViewById(R.id.lbl_title);
        collectionStatus = view.findViewById(R.id.collection_status);
        Button doneBtn = view.findViewById(R.id.done_btn);

        ownerName.setText(pojo);
        String value = "";

        /*switch (garbageType) {
            case "1":
                value = "House Id";
                break;

            case "2":
                value = "Garbage Point Id";
                break;
            case "3":
                value = "Dump yard Id  ";
                break;
            case "10":
                value = "CT/PT Id  ";
                collectionStatus.setText(R.string.cleaned_successful);
                break;
            case "-1":
                if (EmpType.matches("L"))
                    value = "Liquid waste Id  ";
                else if (EmpType.matches("S"))
                    value = "Street waste Id  ";
                else if (EmpType.matches("N"))
                    value = "House Id";
                break;
            *//*case "4":
                value = "Liquid waste Id  ";
                break;
            case "5":
                value = "Street waste Id  ";
                break;*//*
        }*/
        value = getResources().getString(R.string.string_ctpt_waste_id);
        collectionStatus.setText(getResources().getString(R.string.cleaned_successful));
        houseTitle.setText(value);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                restartPreview();
                finish();
            }
        });

    }

   /* @Override
    public void onToiletTypePopUpDismissed(String toiletId, String toiletType) {
        Log.e(TAG, "onEmpSWMTypePopUpDialogDismissed: " + toiletType);
        startSubmitQRAsyncTask(toiletId, -1, gcType, null, imagePojo.getTNS(), toiletType);

    }*/
}