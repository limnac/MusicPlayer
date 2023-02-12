package com.limnac.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.activitys.MainActivity;
import com.limnac.musicplayer.data.adapter.AlbumAdapter;
import com.limnac.musicplayer.data.adapter.SingerAdapter;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.manager.MusicManager;
import com.limnac.musicplayer.services.PlayService;

import java.util.List;
import java.util.Map;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */
public class ClassifyFragment extends Fragment {

    private static final String TAG = "ClassifyFragment";

    private PlayService mPlayService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassifyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassifyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassifyFragment newInstance(String param1, String param2) {
        ClassifyFragment fragment = new ClassifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPlayService = ((MainActivity)context).getPlayService();
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
//        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classify, container, false);

        initSingersRecyclerView(view);
        initAlbumRecyclerView(view);

        return view;
    }

    private void initAlbumRecyclerView(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_albums_fragment_classify);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        Map<String,List<Song>> albumMap = MusicManager.getInstance().getAlbumMap();
        AlbumAdapter albumAdapter = new AlbumAdapter(albumMap);
        recyclerView.setAdapter(albumAdapter);
    }

    private void initSingersRecyclerView(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_singers_fragment_classify);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        Map<String,List<Song>> singerMap = MusicManager.getInstance().getSingerMap();
        SingerAdapter adapter = new SingerAdapter(singerMap);
        recyclerView.setAdapter(adapter);
    }
}