package com.clayton.dribble;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agilie.dribbblesdk.domain.Shot;
import com.clayton.dribble.util.ImageLoaderTask;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.client.util.Strings;

import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Logger;

/**
 * Created by clayton on 01/09/16.
 */
public class FeedListAdapter extends ArrayAdapter<Shot> {
    protected final static Logger log = Logger.getLogger(FeedListAdapter.class.getName());

    private Bitmap dribbledefault;

    public FeedListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FeedListAdapter(Context context, int resource, List<Shot> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_feedpost, parent, false);
        }

        Shot shot = getItem(position);

        if(shot != null) {
            FeedShotView feedShotView = new FeedShotView(this.getContext(), view, shot);
            feedShotView.createView();
        }

        return view;
    }

    class FeedShotView {
        private Context context;
        private View view;
        private Shot shot;

        private Dialog dialog;

        public FeedShotView(Context context, View view, Shot shot) {
            this.context = context;
            this.view = view;
            this.shot = shot;
        }

        public void createView() {
            ImageView imageview = (ImageView) view.findViewById(R.id.feedpostimage);

            if(dribbledefault == null) dribbledefault = BitmapFactory.decodeResource(context.getResources(), R.drawable.dribbledefault);
            imageview.setImageBitmap(dribbledefault);

            ImageLoaderTask.getInstance(shot.getImages().getTeaser(), imageview).execute();
            TextView textView = (TextView) view.findViewById(R.id.feedpostuser);
            textView.setText(shot.getUser().getUserName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(context);

                    dialog.setContentView(R.layout.view_shotdetail);
                    dialog.setTitle(shot.getTitle());
                    ImageView shotdetailImage = (ImageView)dialog.findViewById(R.id.shotdetailimage);
                    shotdetailImage.setImageBitmap(dribbledefault);
                    ImageLoaderTask.getInstance(shot.getImages().getNormal(), shotdetailImage).execute();
                    TextView shotdetailUser = (TextView)dialog.findViewById(R.id.shotdetailuser);
                    shotdetailUser.setText(context.getString(R.string.shotinfo_author) + shot.getUser().getName());
                    TextView shotdetailsocialinfos = (TextView)dialog.findViewById(R.id.shotdetailsocialinfo);
                    String socialtext = context.getString(R.string.shotinfo_socialinfo);
                    socialtext = socialtext.replace("commentscount", ""+shot.getCommentsCount()).replace("likescount", ""+shot.getLikesCount());
                    shotdetailsocialinfos.setText(socialtext);
                    TextView shotdetailldesc = (TextView)dialog.findViewById(R.id.shotdetaildescription);
                    if(!Strings.isNullOrEmpty(shot.getDescription())) {
                        shotdetailldesc.setText(Html.fromHtml(shot.getDescription()));
                    }
                    else shotdetailldesc.setHeight(0);
                    dialog.show();
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        }
                    }, 100);

                    log.info("Clicked: " + shot.getUser().getUserName() + " " + shot.getTitle());
                }
            });
        }
    }
}
