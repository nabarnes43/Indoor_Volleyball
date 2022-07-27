package com.example.indoor_volleyball.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ItemGymBinding;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.text.SimpleDateFormat;
import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.ViewHolder> {
    private Context context;
    private List<Gym> gyms;
    private SimpleDateFormat dateAdapter = CreateEventActivity.dateFormat;


    public GymAdapter(Context context, List<Gym> gyms) {
        this.context = context;
        this.gyms = gyms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(ItemGymBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gym gym = gyms.get(position);
        //Every gym needs an event
        holder.binding.getRoot().setTag(gym);
        try {
            holder.bind(gym);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gyms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemGymBinding binding;
        public ViewHolder(ItemGymBinding b) {
            super(b.getRoot());
            binding = b;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Gym gym = (Gym) v.getTag();
                    if (gym != null) {
                        goToGymDetails(v.getContext(), gym);
                    }
                }
            });
        }

        //TODO date formatter to make the dates look better.
        public void bind(Gym gym) throws ParseException {
            if (gym.getNextEvent() != null) {
                binding.tvEventDateDescription.setText("next event date: " + dateAdapter.format(gym.getNextEvent().getStartTime()) + "\ndetails: " + gym.getNextEvent().getDetails().toLowerCase());
            } else {
                binding.tvEventDateDescription.setText(R.string.no_events_at_gym);
            }
            binding.tvGymName.setText(gym.getName().toLowerCase());
            binding.rbGymRating.setRating(gym.getRating().floatValue());
            ParseFile image = gym.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhoto);
            } else {
                Glide.with(context).load(R.drawable.icon_gym_black).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhoto);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        gyms.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Gym> gymList) {
        gyms.addAll(gymList);
        notifyDataSetChanged();
    }

    public void goToGymDetails(Context context, Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = GymDetailActivity.newIntent(context, gymId);
        context.startActivity(i);
    }


}