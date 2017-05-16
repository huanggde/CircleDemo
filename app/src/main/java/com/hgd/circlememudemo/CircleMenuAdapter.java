package com.hgd.circlememudemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：huanggd on 2017/5/11 16:37
 */

public class CircleMenuAdapter extends BaseAdapter {
    List<MemuItem> mItemlist;

    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public CircleMenuAdapter(List<MemuItem> mItemlist, Context mContext) {
        this.mItemlist = mItemlist;
        this.mContext = mContext;
    }

    public CircleMenuAdapter(List<MemuItem> mItemlist, Context mContext, ImageView imageView) {
        this.mItemlist = mItemlist;
        this.mContext = mContext;
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    ImageView imageView;

    public CircleMenuAdapter(List<MemuItem> mItemlist) {
        this.mItemlist = mItemlist;
    }

    public CircleMenuAdapter(List<MemuItem> mItemlist, ImageView imageView) {
        this.mItemlist = mItemlist;
        this.imageView = imageView;
    }

    @Override
    public int getCount() {
        return mItemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.circle_menu_item, parent, false);

        initItem(itemView, position);
        return itemView;

    }

    public interface OnItemClickListener {
        void onItemClick(Adapter parent, MemuItem view, int position, long id);

    }

    private void initItem(View item, final int position) {
        final MemuItem menuitem = (MemuItem) getItem(position);
        ImageView iv = (ImageView) item.findViewById(R.id.iv_item);
        TextView tv = (TextView) item.findViewById(R.id.tv_item);

        iv.setImageResource(menuitem.MagineId);
        tv.setText(menuitem.title);
        item.findViewById(R.id.circle_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(CircleMenuAdapter.this, menuitem, position, position);
            }
        });


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public static class MemuItem {


        public int MagineId;
        public String title;
        private Class<?> activity;

        public int getMagineId() {
            return MagineId;
        }

        public void setMagineId(int magineId) {
            MagineId = magineId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Class<?> getActivity() {
            return activity;
        }

        public void setActivity(Class<?> activity) {
            this.activity = activity;
        }

        public MemuItem(int magineId, String title, Class<?> activity) {
            MagineId = magineId;
            this.title = title;
            this.activity = activity;
        }


    }
}
