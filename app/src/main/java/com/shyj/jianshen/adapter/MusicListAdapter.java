package com.shyj.jianshen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.bean.MusicItemBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.utils.SaveUtils;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

import static com.shyj.jianshen.key.IntentId.TAG;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {

    private List<MusicItemBean>  musicItemBeanList;

    public void addMusicItemBeanList(List<MusicItemBean> musicItemBeanList) {
        this.musicItemBeanList = musicItemBeanList;
        notifyDataSetChanged();
    }

    private Context mContext;

    public MusicListAdapter(Context context){
        this.mContext = context;
        musicItemBeanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adater_music_list,parent,false);
        return new MusicListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListViewHolder holder, int position) {
        try {
            if (musicItemBeanList!=null&& musicItemBeanList.size()>position){
                MusicItemBean musicItemBean = musicItemBeanList.get(position);
                holder.tvName.setText(musicItemBean.getName());
                Glide.with(mContext).load(musicItemBean.getMusicBg()).into(holder.imgBg);
                if (musicItemBean.isDownload()){
                    holder.imgState.setImageResource(R.mipmap.icon_green_select);
                    holder.imgState.setEnabled(false);
                }else {
                    holder.imgState.setImageResource(R.mipmap.icon_green_download);
                    holder.imgState.setEnabled(true);
                    holder.imgState.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            downloadFile(position,musicItemBean,holder);
                        }
                    });
                }
                holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (musicItemBean.isDownload()){
                            if (onMusicClick!=null){
                                onMusicClick.onMusicClick(position);
                            }else {
                                if (holder.colorfulRingProgressView.getVisibility() == View.GONE){
                                    downloadFile(position,musicItemBean,holder);
                                }
                            }
                        }
                    }
                });
            }
        }catch (Exception e){
            Log.e(TAG, "MusicListAdapter:catch " +e.getMessage() );
        }
    }

    private void downloadFile(int pos,MusicItemBean musicItemBean,MusicListViewHolder holder){
        holder.imgState.setVisibility(View.GONE);
        holder.colorfulRingProgressView.setVisibility(View.VISIBLE);
        if (onMusicClick!=null){
            onMusicClick.onDownload(pos,musicItemBean.getMusicFile(),holder);
        }
/*        try {

            URL url = new URL(musicItemBean.getMusicFile());
            InputStream inputStream = url.openStream();
            String file  = SaveUtils.BASE_FILE_URL+"/music/"+musicItemBean.getMusicFile();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public interface OnMusicClick{
        void onMusicClick(int pos);
        void onDownload(int pos,String url,MusicListViewHolder musicListViewHolder);
    }

    private OnMusicClick onMusicClick;

    public void setOnMusicClick(OnMusicClick onMusicClick) {
        this.onMusicClick = onMusicClick;
    }

    @Override
    public int getItemCount() {
        if (musicItemBeanList!=null&&musicItemBeanList.size()>0){
            return musicItemBeanList.size();
        }
        return 0;
    }

    public class MusicListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imgBg, imgState;
        public ColorfulRingProgressView colorfulRingProgressView;

        public MusicListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_music_list_tv_name);
            imgBg = itemView.findViewById(R.id.item_music_list_img_bg);
            imgState = itemView.findViewById(R.id.item_music_list_img_state);
            colorfulRingProgressView = itemView.findViewById(R.id.item_music_list_progress);
        }
    }
}
