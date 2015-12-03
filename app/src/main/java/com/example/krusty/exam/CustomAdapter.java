package com.example.krusty.exam;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.util.DisplayMetrics;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter{

    List<Integer> counter = new ArrayList<Integer>();

    private List<String> result,result1,result2;
    Context context;
    int i=0;
    private static LayoutInflater inflater=null;

    public CustomAdapter(MainActivity mainActivity, List<String> prgmNameList,List<String> prgmNameList1, List<String> prgmNameList2) {
        result=prgmNameList;
        result1=prgmNameList1;
        result2=prgmNameList2;
        context=mainActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
      //  return result.length;
        return result.size();
        //      return result1.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
      //  return position;
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView tv1;
        TextView tv2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       final Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.item, null);

        holder.tv=(TextView) rowView.findViewById(R.id.item_text1);
        holder.tv1=(TextView) rowView.findViewById(R.id.item_text2);
        holder.tv2=(TextView) rowView.findViewById(R.id.item_text3);

        holder.tv.setText(result.get(position));
        holder.tv1.setText(result1.get(position));
        holder.tv2.setText(result2.get(position));


        // Change the color
        if (holder.tv2.getText().toString().contains("Only")) {
            BlinkTask task = new BlinkTask(holder.tv2);
            task.execute();


        }




        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Toast toast = Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 500);

            }
        });

        return rowView;
    }

    public class BlinkTask extends AsyncTask<String, Void, String> {
        Handler m_handler;
        Runnable m_handlerTask;

        @Override
        protected String doInBackground(String... params) {
return null;
        }

        public BlinkTask(final TextView h) {

           m_handler = new Handler();


        m_handlerTask = new Runnable() {
                private int b = 0;

                @Override
                public void run() {

                    if (b == 0) {
                        h.setBackgroundColor(Color.parseColor("#444444"));
                        b = 1;
                    } else {
                        h.setBackgroundColor(Color.parseColor("#c62104"));
                        b = 0;
                    }



                    // do something. update text view.
                    m_handler.postDelayed(m_handlerTask, 1000);

                }
            };
            m_handlerTask.run();
            i++;
        }


    }

}