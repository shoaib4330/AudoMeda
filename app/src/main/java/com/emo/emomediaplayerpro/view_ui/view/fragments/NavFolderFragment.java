package com.emo.emomediaplayerpro.view_ui.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.FoldersLoader;
import com.emo.emomediaplayerpro.model.domain.entities.Folder;
import com.emo.emomediaplayerpro.view_ui.view.BaseActivity;
import com.emo.emomediaplayerpro.view_ui.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class NavFolderFragment extends Fragment implements FoldersLoader.MediaProviderEventsListener {

    private RecyclerView foldersListView;
    private FolderRecyclerAdapter listAdapter;

    private List<Folder> folderList;
    private FoldersLoader foldersProviderDAO;

    private NavigationManagerContentFlow frag_NavigationManager;
    //private LibraryLeadSelectionEventsListener eventsListener;

    public NavFolderFragment()
    {
        // Required empty public constructor
    }

    public static NavFolderFragment newInstance(String param1, String param2)
    {
        NavFolderFragment fragment = new NavFolderFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context activity)
    {
        super.onAttach(activity);
        if (activity instanceof BaseActivity)
        {
            frag_NavigationManager = (NavigationManagerContentFlow) ((BaseActivity) activity).getNavigationManager();
        } else
        {
            throw new RuntimeException(activity.toString()
                    + " problem retrieving Navigation Manager");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        foldersProviderDAO = new FoldersLoader(getContext(), getLoaderManager());
        foldersProviderDAO.requestFoldersData();
        listAdapter = new FolderRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Folder number: " + (int) v.getTag(), Toast.LENGTH_SHORT).show();
                frag_NavigationManager.startListTracksFragment((folderList.get((int) v.getTag())).getPath(), null, null, null);
                //frag_NavigationManager.startListTracksFragment(folderList.get((int) v.getTag()));
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        /* shoaib: register to the provider's data delivery events */
        foldersProviderDAO.register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        /* shoaib: Unregister from the provider's data delivery events */
        foldersProviderDAO.unRegister();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        foldersListView = (RecyclerView) view.findViewById(R.id.list_FoldersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        foldersListView.setLayoutManager(mLayoutManager);
        foldersListView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onListCreated(List<Folder> pFolderList)
    {
        this.folderList = pFolderList;
        this.listAdapter.updateFoldersList(pFolderList);
    }

    private static class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.FolderViewHolder> {

        public static class FolderViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_FolderName, tv_FolderPath, tv_NumSongs, tv_FolderType;
            private ImageView imgView_FolderImage;

            private static View.OnClickListener mOnClickListener;

            public FolderViewHolder(View itemView)
            {
                super(itemView);
                tv_FolderName = (TextView) itemView.findViewById(R.id.tv_FolderName);
                tv_FolderPath = (TextView) itemView.findViewById(R.id.tv_FolderPath);
                tv_NumSongs = (TextView) itemView.findViewById(R.id.tv_NumSongsInFolder);
                tv_FolderType = (TextView) itemView.findViewById(R.id.tv_FolderView_foldertype);
                imgView_FolderImage = (ImageView) itemView.findViewById(R.id.imgView_FolderImage);
            }

            public void bind(Folder folderItem, int position)
            {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_FolderName.setText(folderItem.getDiplayName());
                tv_FolderPath.setText(folderItem.getPath());
                tv_NumSongs.setText(String.valueOf(folderItem.getCountFiles()));
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener)
            {
                mOnClickListener = listener;
            }
        }

        private List<Folder> adapterFolderList;

        public FolderRecyclerAdapter(View.OnClickListener listener)
        {
            FolderViewHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<Folder> foldersList)
        {
            this.adapterFolderList = foldersList;
            notifyDataSetChanged();
        }

        @Override
        public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_folder_view, parent, false);
            FolderViewHolder fvh = new FolderViewHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(FolderViewHolder holder, int position)
        {
            holder.bind(this.adapterFolderList.get(position), position);
        }

        @Override
        public int getItemCount()
        {
            if (this.adapterFolderList != null)
                return this.adapterFolderList.size();
            return 0;
        }
    }
}
