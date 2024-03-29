package com.ahmadfauzirahman.sakato.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.ahmadfauzirahman.sakato.R;
import com.ahmadfauzirahman.sakato.adapter.SpmAdapter;
import com.ahmadfauzirahman.sakato.model.SpmModel;
import com.ahmadfauzirahman.sakato.response.SpmResponse;
import com.ahmadfauzirahman.sakato.rest.ApiClient;
import com.ahmadfauzirahman.sakato.rest.ApiInterface;
import com.ahmadfauzirahman.sakato.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpmFragment extends Fragment {

    public SpmFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    SessionManager sessionManager;
    String stakeholder;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_spm, container, false);

        sessionManager = new SessionManager(getContext());
        swipeRefreshLayout = view.findViewById(R.id.swpspm);
        recyclerView = (RecyclerView) view.findViewById(R.id.reyspm);
        stakeholder = sessionManager.getUserDetail().get("penUsername");

        all(stakeholder);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // loading = ProgressDialog.show(context,null,"Sedang mendapatkan berita",true,false);
                swipeRefreshLayout.setRefreshing(false);
                all(stakeholder);

            }
        });

        return view;
    }

    private void all(String stakeholder) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reyspm);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        apiService.spmbykd(stakeholder).enqueue(new Callback<SpmResponse>() {
            @Override
            public void onResponse(Call<SpmResponse> call, Response<SpmResponse> response) {
                System.out.println("OnResponse Url" + response.toString());
                System.out.println("OnResponse Data" + response.body().toString());
                if (response.isSuccessful()) {
                    List<SpmModel> spmModels = response.body().getData();
                    recyclerView.setAdapter(new SpmAdapter(spmModels, R.layout.list_spm, getContext()));
                } else {
                    System.out.println("OnResponse Data" + response.body().toString());

                    Toast.makeText(getContext(), "Tidak Terhubung KeJaringan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpmResponse> call, Throwable t) {
                System.out.println("Error Aplikasi" +
                        "" + t.getLocalizedMessage());

            }
        });

    }
}
