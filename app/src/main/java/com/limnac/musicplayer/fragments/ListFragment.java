package com.limnac.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.limnac.musicplayer.activitys.MainActivity;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.activitys.PlayActivity;
import com.limnac.musicplayer.adapter.SongAdapter;
import com.limnac.musicplayer.model.Song;
import com.limnac.musicplayer.services.PlayService;
import com.limnac.musicplayer.utils.MusicUtil;

import java.util.List;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */
public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    private static List<Song> mSongList;
    private ListView mSongListView;

    private PlayService mPlayService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPlayService = ((MainActivity)context).getPlayService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mSongList = MusicUtil.getmusic(getContext());
        mSongListView = view.findViewById(R.id.songs_listview);
        SongAdapter adapter = new SongAdapter(mSongList,getContext());
        mSongListView.setAdapter(adapter);
        mSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPlayService.playNewMusic(i);
                PlayActivity.startPlayActivity(getContext());
            }
        });
        return view;
    }

    public  static List<Song> getSongList(){
        return mSongList;
    }
}