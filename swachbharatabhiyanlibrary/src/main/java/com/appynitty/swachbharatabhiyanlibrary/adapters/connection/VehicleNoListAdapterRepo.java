package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.util.Log;
import android.widget.BaseAdapter;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaHousePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleNumberPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.webservices.VehicleTypeWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*****
 * created by Rahul rokade
 * vehicle number list and vehicle QR id list enter manually api repository
 * */

public class VehicleNoListAdapterRepo {
    private static final String TAG = "VehicleNoListAdapterRepo";

    private static final VehicleNoListAdapterRepo instance = new VehicleNoListAdapterRepo();

    public static VehicleNoListAdapterRepo getInstance() {
        return instance;
    }

    public void getVehicleNosList(String appId, String vehicleTypeId, IVehicleNoListListener iVehicleNoListListener) {
        VehicleTypeWebService service = Connection.createService(VehicleTypeWebService.class, AUtils.SERVER_URL);
        Call<List<VehicleNumberPojo>> vehicleNosListCall = service.pullVehicleNumberList(AUtils.CONTENT_TYPE, appId, vehicleTypeId);
        vehicleNosListCall.enqueue(new Callback<List<VehicleNumberPojo>>() {
            @Override
            public void onResponse(Call<List<VehicleNumberPojo>> call, Response<List<VehicleNumberPojo>> response) {
                iVehicleNoListListener.onResponse(response.body());
                Log.e(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<VehicleNumberPojo>> call, Throwable t) {
                iVehicleNoListListener.onFailure(t);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void getVehicleQRIdList(String appId, IVehicleQRIdListListener iVehicleQRIdListListener) {
        VehicleTypeWebService service = Connection.createService(VehicleTypeWebService.class, AUtils.SERVER_URL);
        Call<List<CollectionAreaHousePojo>> vehicleNosListCall = service.pullVehicleQRIdList(appId,AUtils.CONTENT_TYPE,"","0","V");
        vehicleNosListCall.enqueue(new Callback<List<CollectionAreaHousePojo>>() {
            @Override
            public void onResponse(Call<List<CollectionAreaHousePojo>> call, Response<List<CollectionAreaHousePojo>> response) {
                iVehicleQRIdListListener.onResponse(response.body());
                Log.e(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<CollectionAreaHousePojo>> call, Throwable t) {
                iVehicleQRIdListListener.onFailure(t);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }



    public interface IVehicleQRIdListListener{
        void onResponse(List<CollectionAreaHousePojo> vehicleQRIdList);

        void onFailure(Throwable throwable);
    }


    public interface IVehicleNoListListener {
        void onResponse(List<VehicleNumberPojo> vehicleNumbersList);

        void onFailure(Throwable throwable);
    }
}
