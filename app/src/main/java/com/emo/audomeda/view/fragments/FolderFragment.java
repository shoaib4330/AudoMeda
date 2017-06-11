package com.emo.audomeda.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emo.audomeda.R;


public class FolderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView foldersListView;
    private FolderRecyclerAdapter listAdapter;

    public FolderFragment() {
        // Required empty public constructor
    }

    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        listAdapter = new FolderRecyclerAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        foldersListView = (RecyclerView) view.findViewById(R.id.list_FoldersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        foldersListView.setLayoutManager(mLayoutManager);
        foldersListView.setAdapter(listAdapter);
        return view;
    }

    private  static class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.FolderViewHolder>{

        public static class FolderViewHolder extends RecyclerView.ViewHolder{
            public TextView tv_FolderName, tv_FolderPath, tv_NumSongs;
            public ImageView imgView_FolderImage;

            public FolderViewHolder(View itemView) {
                super(itemView);
                tv_FolderName = (TextView) itemView.findViewById(R.id.tv_FolderName);
                tv_FolderPath = (TextView) itemView.findViewById(R.id.tv_FolderPath);
                tv_NumSongs   = (TextView) itemView.findViewById(R.id.tv_NumSongsInFolder);
                imgView_FolderImage = (ImageView) itemView.findViewById(R.id.imgView_FolderImage);
            }
        }

        @Override
        public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.folder_view,parent,false);
            FolderViewHolder fvh = new FolderViewHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(FolderViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
