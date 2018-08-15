package xyz.okxy.everydaygank;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.okxy.everydaygank.bean.Item;
import xyz.okxy.everydaygank.util.FetchItems;

public class ListFragment extends Fragment {

    private static final String ARG_URL_STRING = "arg_url_string";

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ProgressBar mProgressBar;
    private String mCategoryUrl;
    private int mPage;

    public static ListFragment newInstance(String categoryUrl) {
        ListFragment fragment = new ListFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_URL_STRING, categoryUrl);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryUrl = getArguments().getString(ARG_URL_STRING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        mProgressBar = view.findViewById(R.id.list_progress_bar);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // 垂直布局

        // 拉到列表底部则增加数据
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    mProgressBar.setVisibility(View.VISIBLE); // 显示加载进度条
                    updateItems();
                }
            }
        });

        //获取JSON数据并返回JavaBean对象
        updateItems();

        return view;
    }

    // 更新RecyclerView的适配器内容
    private void updateItems() {
        new SyncTask().execute();
    }

    // 用于异步加载 获取数据并更新数据
    private class SyncTask extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... voids) {
            return new FetchItems().Fetch(mCategoryUrl, ++mPage);
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            if(items == null) {
                Toast.makeText(getActivity(), "网络连接有问题哦~", Toast.LENGTH_SHORT).show();
                return;
            } else if (mAdapter == null) {
                mAdapter = new Adapter(items);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                int positionStart = mAdapter.mItems.size();
                mAdapter.mItems.addAll(items);
                mAdapter.notifyItemRangeInserted(positionStart, positionStart+10);
                mRecyclerView.smoothScrollToPosition(positionStart+3);
            }

            // 加载完成后进度条消失
            mProgressBar.setVisibility(View.GONE);
        }

    }

    // [内部类] RecyclerView的子项容器
    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mAuthor;
        TextView mPublishedAt;
        ImageView mCardViewBg;

        public ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.card_view_title);
            mAuthor = itemView.findViewById(R.id.card_view_author);
            mPublishedAt = itemView.findViewById(R.id.card_view_published_at);
            mCardViewBg = itemView.findViewById(R.id.card_view_bg);
        }

        public void bind(final Item item) {
            mTitle.setText(item.mDesc);
            mAuthor.setText(item.mWho);
            mPublishedAt.setText(item.mPublishedAt.substring(0, 10));

            // 有图片就加载一张
            if (null != item.mImages) {
                mTitle.setEms(10); // 且设置标题的一行长度
                Glide.with(getActivity())
                        .load(item.mImages.get(0))
                        .into(mCardViewBg);
            }

            // 点击后调用浏览器
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(item.mUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

    // [内部类] RecyclerView的适配器
    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Item> mItems;

        public Adapter(List<Item> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_recycler_card_view, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Item item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        // 当View被回收时取消图片加载，防止图片加载到新的View里导致乱序
        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            Glide.with(getActivity()).clear(holder.mCardViewBg);
        }
    }

}
