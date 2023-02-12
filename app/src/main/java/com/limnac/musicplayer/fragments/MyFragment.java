package com.limnac.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.activitys.StartActivity;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */
public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        Button buttonScan = view.findViewById(R.id.scan_button_fragment_my);
        buttonScan.setOnClickListener(v->scanLocalMusic());
        return view;
    }

    private void scanLocalMusic() {
        StartActivity.startActivity(getContext());
        requireActivity().finish();
//        MusicManager.getInstance().update(new UpdateCallBack() {
//            @Override
//            public void success() {
//                Looper.prepare();
//                Toast toast =  Toast.makeText(getActivity(),"音乐加载结束",Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER,0,0);
//                toast.show();
//                Looper.loop();
//            }
//
//            @Override
//            public void onFailed(int errorCode, String msg) {
//                Looper.prepare();
//                Toast toast =  Toast.makeText(getActivity(),"音乐加载失败",Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER,0,0);
//                toast.show();
//                Looper.loop();
//            }
//        });
    }
}