package chapter.android.aweme.ss.com.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.model.MyAdapter;
import chapter.android.aweme.ss.com.homework.model.PullParser;

/**
 * 大作业:实现一个抖音消息页面,所需资源已放在res下面
 */
public class Exercises3 extends AppCompatActivity implements MyAdapter.ListItemClickListener {
    private MyAdapter mAdapter;
    private RecyclerView recyclerView;
    List<Message> messages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_list_item );
        recyclerView = findViewById(R.id.rv_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        try {
            InputStream assetInput = getAssets().open("data.xml");
            messages = PullParser.pull2xml(assetInput);
            mAdapter = new MyAdapter(messages,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // 最后一个完全可见项的位置
            private int lastCompletelyVisibleItemPosition;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (visibleItemCount > 0 && lastCompletelyVisibleItemPosition >= totalItemCount - 1) {
                        Toast.makeText(Exercises3.this, "已滑动到底部!,触发loadMore", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }
                //Log.d(TAG, "onScrolled: lastVisiblePosition=" + lastCompletelyVisibleItemPosition);
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //Log.d("getin","ok");
        Intent intent = new Intent(this,ChatRoom.class);
        intent.putExtra("name",messages.get(clickedItemIndex).getTitle().toString());
        startActivity(intent);
    }

    public class ChatRoom extends AppCompatActivity {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chatroom);


            TextView tv_with_name;
            final TextView ed_say;
            ImageView btn_send_info;
            final TextView tv_content_info;

            ed_say = findViewById(R.id.ed_say);
            tv_with_name = findViewById(R.id.tv_with_name);
            btn_send_info = findViewById(R.id.btn_send_info);
            tv_content_info = findViewById(R.id.tv_content_info);

            Intent i=getIntent();
            tv_with_name.setText(i.getStringExtra("name"));

            btn_send_info.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tv_content_info.setText(ed_say.getText().toString()+"\n\n");
                    ed_say.setText(null);
                }
            });


        }
    }
}
