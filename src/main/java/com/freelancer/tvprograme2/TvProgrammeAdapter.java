package com.freelancer.tvprograme2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freelancer.tvprograme2.dto.TvProgramme;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android 18 on 4/22/2016.
 */
public class TvProgrammeAdapter extends RecyclerView.Adapter<TvProgrammeAdapter.TvProgrammeViewHolder> {

    private List<TvProgramme.ProgrammDataObject> mTvProgrammes = new ArrayList<>();

    /**
     * Set the tv programmes list. This replaces the current list with the new one in the parameter and calls {@link android.support.v7.widget.RecyclerView.Adapter#notifyDataSetChanged()}
     *
     * @param tvProgrammes
     */
    public void setTvProgrammes(List<TvProgramme.ProgrammDataObject> tvProgrammes) {
        mTvProgrammes = tvProgrammes;
        notifyDataSetChanged();
    }

    /**
     * Append a list of tv programmes to the current list and calls {@link android.support.v7.widget.RecyclerView.Adapter#notifyDataSetChanged()}.
     *
     * @param tvProgrammes
     */
    public void appendTvProgrammes(List<TvProgramme.ProgrammDataObject> tvProgrammes) {
        mTvProgrammes.addAll(tvProgrammes);
        notifyDataSetChanged();
    }

    @Override
    public TvProgrammeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvProgrammeViewHolder(View.inflate(parent.getContext(), R.layout.viewholder_tv_programme, null));
    }

    @Override
    public void onBindViewHolder(TvProgrammeViewHolder holder, int position) {
        holder.setTvProgrammeData(mTvProgrammes.get(position));
    }

    @Override
    public int getItemCount() {
        return mTvProgrammes.size();
    }

    public class TvProgrammeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.programme_name_textview)
        protected TextView name;
        @Bind(R.id.programme_start_time_textview)
        protected TextView start_time;
        @Bind(R.id.programme_end_time_textview)
        protected TextView end_time;
        @Bind(R.id.programme_channel_textview)
        protected TextView channel;
        @Bind(R.id.programme_rating_textview)
        TextView rating;

        public void setTvProgrammeData(TvProgramme.ProgrammDataObject programme) {
            name.setText(programme.getName());
            start_time.setText(programme.getStart_time());
            end_time.setText(programme.getEnd_time());
            channel.setText(programme.getChannel());
            rating.setText(programme.getRating());
        }

        public TvProgrammeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
