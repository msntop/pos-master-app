package com.odoo.addons.pos;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.Container;
import com.odoo.R;
import com.odoo.addons.customers.CustomerDetails;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.utils.IntentUtils;
import com.odoo.core.utils.OFragmentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Harshad on 8/7/2015.
 */
public class Order extends AppCompatActivity {
    private static final String TAG = Order.class.getName();
    public OrderAdapter adapter;
    public TextView tvPrdctname;
    public TextView tvPrize;
    TextView grandTotalPrize;
    private Context context;
    ArrayList<PosOrder> array;
     ListView list;
    TextView total;
    public EditText etPrdctQumtity;
    public EditText etPrdctPrize;
    public EditText etPrdctDiscount;
    public ImageView imageProduct;
    Button btnContinueShop;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_listview);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("POS");
        }

        list = (ListView) findViewById(R.id.list_order);
       tvPrdctname = (TextView) findViewById(R.id.productName);
        grandTotalPrize = (TextView) findViewById(R.id.grandTotal);
        etPrdctQumtity = (EditText) findViewById(R.id.prdctQuantity);
       btnContinueShop=(Button)findViewById(R.id.btnContinue);
        etPrdctPrize = (EditText) findViewById(R.id.prdctPrize);
        etPrdctDiscount = (EditText) findViewById(R.id.discount);
        tvPrize = (TextView) findViewById(R.id.NetPrize);
        imageProduct=(ImageView)findViewById(R.id.productImage);
        array = new ArrayList<PosOrder>();

        array = (ArrayList<PosOrder>) getIntent().getSerializableExtra("cart_details");
        adapter = new OrderAdapter(this, R.layout.order_single_row, array);
        list.setAdapter(adapter);
        list.setDividerHeight(0);

      btnContinueShop.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            finish();
          }
      });



        NetAmount();

        final TextView tax = (TextView) findViewById(R.id.taxesTotal);
        tax.setText("0.0");

    }

    public void NetAmount() {

        float grandTotal = 0.0f;

        for (int i = 0; i < adapter.getCount(); i++) {
            PosOrder pos = adapter.getItem(i);
            float Discount = (pos.getProductPrize() * pos.getDiscount() / 100);
            float netPrice = ((pos.getProductPrize() - Discount));
            float productTotalAmount = pos.getProductQntity() * netPrice;
            grandTotal += productTotalAmount;
        }
        grandTotalPrize.setText(grandTotal + "");
    }


    public class OrderAdapter extends ArrayAdapter<PosOrder> {
        private List<PosOrder> orderItem;

        private Context context;

        TextView Rs;


        public OrderAdapter(Context context, int textViewResourceId,
                            List<PosOrder> orderItem) {
            super(context, textViewResourceId, orderItem);
            this.context = context;
            this.orderItem = orderItem;

        }

        public class ViewHolder {
            TextView prdctName;
            TextView prdctNetAmount;
            EditText prdctQuantity;
            EditText prdctPrize;
            EditText prdctDiscount;
            ImageButton imgDlt;
            ImageView prdctImage;
        }


        @Override
        public View getView( int position, View view, ViewGroup parent) {

            final ViewHolder holder;
            holder = new ViewHolder();
            PosOrder posOrder = orderItem.get(position);
            float Discount = (posOrder.getProductPrize() * posOrder.getDiscount() / 100);
            float netPrice = ((posOrder.getProductPrize() - Discount));
            float productTotalAmount = posOrder.getProductQntity() * netPrice;
            posOrder.setNetAmount(productTotalAmount);
            if (view == null) {

                LayoutInflater vi = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.order_single_row, null);
            }
                holder.prdctQuantity = (EditText) view.findViewById(R.id.prdctQuantity);
                holder.prdctQuantity.setTag(posOrder);

                holder.prdctQuantity.setText(String.valueOf(posOrder.getProductQntity()));

                holder.prdctImage = (ImageView) view.findViewById(R.id.productImage);
                holder.prdctImage.setTag(posOrder);
                Bitmap bmp = BitmapFactory.decodeByteArray(posOrder.getImage(), 0, posOrder.getImage().length);
                holder.prdctImage.setImageBitmap(bmp);


                holder.imgDlt = (ImageButton) view.findViewById(R.id.Imagedelete);
                holder.imgDlt.setTag(posOrder);


                holder.prdctName = (TextView) view.findViewById(R.id.productName);
                holder.prdctName.setTag(posOrder);
                holder.prdctName.setText(posOrder.getProductName());


                holder.prdctPrize = (EditText) view.findViewById(R.id.prdctPrize);
                holder.prdctPrize.setTag(posOrder);
                holder.prdctPrize.setText(String.valueOf(posOrder.getProductPrize()));


                holder.prdctDiscount = (EditText) view.findViewById(R.id.discount);
                holder.prdctDiscount.setTag(posOrder);
                holder.prdctDiscount.setText(String.valueOf(posOrder.getDiscount()));

                holder.prdctNetAmount = (TextView) view.findViewById(R.id.NetPrize);
                holder.prdctNetAmount.setText(String.valueOf(posOrder.getNetAmount()));



                holder.imgDlt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                Order.this);
                        alert.setTitle("Delete");
                        alert.setMessage(R.string.delete_messege);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PosOrder ps = (PosOrder) holder.imgDlt.getTag();

                                orderItem.remove(ps);
                                adapter.notifyDataSetChanged();
                                list.setAdapter(adapter);
                                NetAmount();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        alert.show();

                    }
                });


                holder.prdctQuantity.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        PosOrder pos = (PosOrder) holder.prdctQuantity.getTag();
                        System.out.println("Quantity =" + pos.getProductQntity());


                        String Quantity = s.toString();
                        System.out.println("Qunt" + Quantity);
                        if (Quantity.matches("")) {
                            pos.setProductQntity(0);

                        } else {
                            pos.setProductQntity(Integer.valueOf(Quantity));
                            float Discount = (pos.getProductPrize() * pos.getDiscount() / 100);
                            float netPrice = ((pos.getProductPrize() - Discount));
                            float productTotalAmount = pos.getProductQntity() * netPrice;
                            pos.setNetAmount(productTotalAmount);
                            System.out.println(pos.getNetAmount() + "Price");
                            holder.prdctNetAmount.setText(String.valueOf(productTotalAmount));
                            holder.prdctName.setText(pos.getProductName());
                            NetAmount();

                        }

                    }


                    @Override
                    public void afterTextChanged(Editable s) {


                    }

                });

                holder.prdctPrize.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        PosOrder pos = (PosOrder) holder.prdctPrize.getTag();
                        String Price = holder.prdctPrize.getText().toString();
                        System.out.println("Qunt" + Price);
                        if (Price.matches("")) {
                            pos.setProductPrize(0);

                        } else {
                            pos.setProductPrize(Float.valueOf(Price));
                            float Discount = (pos.getProductPrize() * pos.getDiscount() / 100);
                            float netPrice = ((pos.getProductPrize() - Discount));
                            float productTotalAmount = pos.getProductQntity() * netPrice;
                            pos.setNetAmount(productTotalAmount);
                            System.out.println(pos.getNetAmount() + "Price");
                            holder.prdctNetAmount.setText(String.valueOf(productTotalAmount));
                            holder.prdctName.setText(pos.getProductName());
                            NetAmount();
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                holder.prdctDiscount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        PosOrder pos = (PosOrder) holder.prdctDiscount.getTag();
                        String discount1 = holder.prdctDiscount.getText().toString();
                        System.out.println("Qunt" + discount1);
                        if (discount1.matches("")) {
                            pos.setDiscount(0);

                        } else {
                            pos.setDiscount(Float.valueOf(discount1));
                            float Discount = (pos.getProductPrize() * pos.getDiscount() / 100);
                            float netPrice = ((pos.getProductPrize() - Discount));
                            float productTotalAmount = pos.getProductQntity() * netPrice;
                            pos.setNetAmount(productTotalAmount);
                            System.out.println(pos.getNetAmount() + "Price");
                            holder.prdctNetAmount.setText(String.valueOf(productTotalAmount));
                            holder.prdctName.setText(pos.getProductName());
                            NetAmount();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            return view;
        }
    }
}




