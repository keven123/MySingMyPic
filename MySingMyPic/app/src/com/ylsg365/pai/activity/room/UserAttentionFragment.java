package com.ylsg365.pai.activity.room;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ylsg365.pai.R;
import com.ylsg365.pai.activity.base.TabFragment;
import com.ylsg365.pai.app.YinApi;
import com.ylsg365.pai.event.UserAttentionEvent;
import com.ylsg365.pai.util.JsonUtil;
import com.ylsg365.pai.util.LogUtil;

import de.greenrobot.event.EventBus;

public class UserAttentionFragment extends TabFragment implements AbsListView.OnItemClickListener{
    private String nid;
    private UserAttentionAdapter mAdapter;
    
    // TODO: Rename and change types of parameters
    public static UserAttentionFragment newInstance(String param1, String param2) {
        UserAttentionFragment fragment = new UserAttentionFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserAttentionFragment() {
    }
    
    public UserAttentionFragment(String nid) {
        this.nid = nid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_attention, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            Paint paint = new Paint();

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                super.onDrawOver(c, parent, state);
                paint.setColor(getResources().getColor(R.color.line_radio));
                for (int i = 0, size = parent.getChildCount(); i < size; i++) {
                    View child = parent.getChildAt(i);
                    c.drawLine(child.getLeft() + 20, child.getBottom(), child.getRight(), child.getBottom(), paint);
                }
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        mAdapter = new UserAttentionAdapter();
        recyclerView.setAdapter(mAdapter);
        getViewerList();

        return rootView;
    }
    
    public synchronized void refresh() {
        getViewerList();
    }
    
    private void getViewerList() {
        YinApi.getHouseViewers(nid, 0, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                LogUtil.logd("getViewerList", res);
                JSONObject response = null;
                try {
                    response = new JSONObject(res);
                } catch (JSONException e) {
                }
                if (response != null && JsonUtil.getBoolean(response, "status")) {
                    List<JSONObject> infoList = mAdapter.getList();
                    JSONArray infoJsonArray = JsonUtil.getJSONArray(response, "houses");
                    int length = infoJsonArray.length();
                    for (int i = 0; i < length; i++) {
                        infoList.add(JsonUtil.getJSONObject(infoJsonArray, i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public String getTitle() {
        return "观众";
    }
}
