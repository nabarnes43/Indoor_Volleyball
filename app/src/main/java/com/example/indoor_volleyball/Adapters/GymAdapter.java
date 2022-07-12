package com.example.indoor_volleyball.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.indoor_volleyball.Activities.MainActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;


import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ItemGymBinding;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.ViewHolder> {
    private Context context;
    private List<Gym> gyms;


    public GymAdapter(Context context, List<Gym> gyms) {
        this.context = context;
        this.gyms = gyms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.item_gym, parent, false);

        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(ItemGymBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gym gym = gyms.get(position);
        //Every gym needs an event
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


            //On click listener for item
            //TODO set up detail view.
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Post post = (Post) v.getTag();
//                    if (post!=null) {
//                        Intent i = new Intent(context, PostDetail.class );
//                        i.putExtra("post", Parcels.wrap(post));
//                        context.startActivity(i);
//                    }
//                }
//            });
        }
        //TODO adding bindings made the formatting weird
        public void bind(Gym gym) throws ParseException {
            Event nextEvent = (Event) gym.getNextEvent();
            binding.tvGymName.setText(gym.getName());
            binding.tvEventDateDescription.setText("Date/Time: " + gym.getNextEvent().getStartTime() + "  " + gym.getNextEvent().getEndTime() + " Details: " + gym.getNextEvent().getDetails());
            binding.rbGymRating.setRating(gym.getRating().floatValue());
            binding.tvGymName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).goToGymDetails(gym);
                }
            });

            //TODO Image code
//            ParseFile image = post.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImagePost);
//            }
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


}