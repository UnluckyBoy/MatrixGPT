package com.matrix.matrix_chat.UI.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticleBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticlesBean;
import com.matrix.matrix_chat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @ClassName AllArticlesAdpter
 * @Author Create By matrix
 * @Date 2023/5/11 0011 20:05
 */
public class AllArticlesAdapter extends RecyclerView.Adapter<AllArticlesAdapter.ViewHolder>{
    private List<ArticleBean> articlesBeanList;
    private Activity mActivity;

    public AllArticlesAdapter(Activity mActivity, List<ArticleBean> list){
        this.articlesBeanList =list;
        this.mActivity=mActivity;
    }

    @NonNull
    @Override
    public AllArticlesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mActivity).inflate(R.layout.view_article_list,parent,false);//加载list视图
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllArticlesAdapter.ViewHolder holder, int position) {
        //Toast.makeText(mActivity,"获取的list:"+articlesBeanList.get(position).toString(),Toast.LENGTH_SHORT).show();

        if(articlesBeanList.get(position)!=null){
            SetPic(holder,mActivity.getResources().getText(R.string.BackUrl)+"/get_file/"+ articlesBeanList.get(position).getmCover());

            holder.article_title.setText(articlesBeanList.get(position).getmTitle());
            holder.article_hot.setText(String.valueOf(articlesBeanList.get(position).getmHot()));
            holder.article_writer.setText(articlesBeanList.get(position).getmAuthor());
            holder.article_type.setText(articlesBeanList.get(position).getmType());
            holder.article_des.setText(articlesBeanList.get(position).getmDescription());
            holder.article_time.setText(articlesBeanList.get(position).getmCreateTime());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getLayoutPosition();
                OnItemClickListener.onItemClick(holder.itemView,position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getLayoutPosition();
                OnItemClickListener.onItemLongClick(holder.itemView,position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesBeanList.size();
    }

    /**设置封面**/
    private void SetPic(AllArticlesAdapter.ViewHolder viewHolder, String path){
        if(path!=null){
            Picasso.get()
                    .load(path)
                    .placeholder(R.drawable.warning)
                    .error(R.drawable.warning)
                    .into(viewHolder.article_image);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView article_image;
        private TextView article_hot,article_title,article_writer,article_type,article_des,article_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            article_image=itemView.findViewById(R.id.article_image);
            article_hot=itemView.findViewById(R.id.article_hot);
            article_title=itemView.findViewById(R.id.article_title);
            article_writer=itemView.findViewById(R.id.article_writer);
            article_type=itemView.findViewById(R.id.article_type);
            article_des=itemView.findViewById(R.id.article_des);
            article_time=itemView.findViewById(R.id.article_time);
        }
    }

    public interface OnItemClickListener{
        void  onItemClick(View view, int position);
        void  onItemLongClick(View view, int position);
    }
    private AllArticlesAdapter.OnItemClickListener OnItemClickListener;
    public void SetOnItemClickListener(AllArticlesAdapter.OnItemClickListener onItemClickListener){
        OnItemClickListener =onItemClickListener;
    }
}
