package com.odoo.addons.pos;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.odoo.App;
import com.odoo.R;
import com.odoo.addons.pos.models.PosCategory;
import com.odoo.addons.pos.models.ProductTemplate;
import com.odoo.base.addons.ir.feature.OFileManager;
import com.odoo.base.addons.res.ResPartner;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.utils.BitmapUtils;
import com.odoo.core.utils.OActionBarUtils;
import com.odoo.core.utils.OStringColorUtil;

import odoo.controls.OField;
import odoo.controls.OForm;
import odoo.helper.OdooFields;
import odoo.helper.utils.gson.OdooRecord;

/**
 * Created by My on 8/12/2015.
 */
public class ProductList extends ActionBarActivity implements View.OnClickListener,
        OField.IOnFieldValueChangeListener {
    public static final String TAG = ProductList.class.getSimpleName();
    private final String KEY_MODE = "key_edit_mode";
    private final String KEY_NEW_IMAGE = "key_new_image";
    private ActionBar actionBar;
    private Bundle extras;
    private ProductTemplate producttemp;
    private PosCategory posCategory;
    private ResPartner resPartner;
    private ODataRow record = null;
    private ScrollView parallaxScrollView;
    private ImageView userImage = null, captureImage = null;
    private TextView mTitleView = null;
    private OForm mForm;
    private App app;
    private Boolean mEditMode = false;
    private Menu mMenu;
    private OFileManager fileManager;
    private String newImage = null;
    // GridView gv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_categ);
        OActionBarUtils.setActionBar(this, false);
        fileManager = new OFileManager(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(KEY_MODE);
            newImage = savedInstanceState.getString(KEY_NEW_IMAGE);
        }
        app = (App) getApplicationContext();
        //  parallaxScrollView = (ScrollView) findViewById(R.id.categoryScrollViewEdit);
        //      parallaxScrollView.setActionBar(actionBar);
        //  gv = (GridView)findViewById(R.id.pos_categ_id);
        userImage = (ImageView) findViewById(android.R.id.icon);
       // mTitleView = (TextView) findViewById(R.id.name);
       // posCategory = new PosCategory(this, null);
        producttemp = new ProductTemplate(this,null);
        extras = getIntent().getExtras();
        if (extras == null)
            mEditMode = true;
        setupActionBar();
    }

    private void setMode(Boolean edit) {
        if (mMenu != null) {
            mMenu.findItem(R.id.menu_category_detail_more).setVisible(!edit);
            mMenu.findItem(R.id.menu_category_edit).setVisible(!edit);
            mMenu.findItem(R.id.menu_category_save).setVisible(edit);
            mMenu.findItem(R.id.menu_category_cancel).setVisible(edit);
        }
        int color = Color.DKGRAY;
        if (record != null) {
            color = OStringColorUtil.getStringColor(this, record.getString("name"));
        }
        if (edit) {
            if (extras != null)
                actionBar.setTitle(R.string.label_edit);
            else
                actionBar.setTitle(R.string.label_new);
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
            mForm = (OForm) findViewById(R.id.categoriesFormEdit);
            // captureImage = (ImageView) findViewById(R.id.captureImage);
//            captureImage.setOnClickListener(this);
            userImage = (ImageView) findViewById(android.R.id.icon1);
            // findViewById(R.id.categoryScrollViewEdit).setVisibility(View.GONE);
            // findViewById(R.id.categoryScrollViewEdit).setVisibility(View.VISIBLE);
          OField name = (OField) findViewById(R.id.name);
//              Name.setOnValueChangeListener(this);
        } else {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_shade));
            userImage = (ImageView) findViewById(android.R.id.icon);
            mForm = (OForm) findViewById(R.id.productFormEdit);
            // findViewById(R.id.categoryScrollViewEdit).setVisibility(View.GONE);
            // findViewById(R.id.categoryScrollViewEdit).setVisibility(View.VISIBLE);
        }
        setColor(color);
    }

    private void setupActionBar() {
        if (extras == null) {
            setMode(mEditMode);
            // userImage.setColorFilter(Color.parseColor("#ffffff"));
            mForm.setEditable(mEditMode);
            mForm.initForm(null);
//            OField field = (OField)findViewById(R.id.parent_id);
//            field.setOnValueChangeListener(this);
            //Toast.makeText(this, "Select Changeding", Toast.LENGTH_LONG).show();


        } else {
            int rowId = extras.getInt(OColumn.ROW_ID);
            record = producttemp.browse(rowId);
            // record.put("full_address", resPartner.getAddress(record));
           // record.put("pos_categ_id",producttemp.getColumns(false));
            checkControls();
            setMode(mEditMode);
            mForm.setEditable(mEditMode);
            mForm.initForm(record);

            // gv.setText(record.getString("pos_categ_id"));
//            setCustomerImage();
//            if (record.getInt("id") != 0 && record.getString("large_image").equals("false")) {
//                BigImageLoader bigImageLoader = new BigImageLoader();
//                bigImageLoader.execute(record.getInt("id"));
//            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.parent_id:
//                IntentUtils.requestMessage(this, record.getString("parent_id"));
//                break;
//            case R.id.pos_categ_id:
//                IntentUtils.requestMessage(this, record.getString("pos_categ_id"));
//                Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
//                           Toast.LENGTH_LONG).show();
//                break;
//            case R.id.email:
//                IntentUtils.requestMessage(this, record.getString("email"));
//                break;
//            case R.id.phone_number:
//                IntentUtils.requestCall(this, record.getString("phone"));
//                break;
//            case R.id.mobile_number:
//                IntentUtils.requestCall(this, record.getString("mobile"));
//                break;
//            case R.id.captureImage:
//                fileManager.requestForFile(OFileManager.RequestType.IMAGE_OR_CAPTURE_IMAGE);
//                break;
        }
    }
    private void checkControls() {
//        findViewById(R.id.parent_id).setOnClickListener(this);
//        // findViewById(R.id.pos_categ_id).setOnClickListener(this);
//        Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
//                Toast.LENGTH_LONG).show();

//        findViewById(R.id.website).setOnClickListener(this);
//        findViewById(R.id.email).setOnClickListener(this);
//        findVViewById(R.id.phone_number).setOnClickListener(this);
//        findiewById(R.id.mobile_number).setOnClickListener(this);
    }


    private void setCustomerImage() {
        if (!record.getString("image_small").equals("false")) {
            userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            userImage.setColorFilter(null);
            String base64 = newImage;
            if (newImage == null) {
                if (!record.getString("large_image").equals("false")) {
                    base64 = record.getString("large_image");
                } else {
                    base64 = record.getString("image_small");
                }
            }
            userImage.setImageBitmap(BitmapUtils.getBitmapImage(this, base64));
        } else {
            // userImage.setColorFilter(Color.parseColor("#ffffff"));
        }
    }

    private void setColor(int color) {
        //  FrameLayout frameLayout = (FrameLayout) findViewById(R.id.parallax_view);
        // frameLayout.setBackgroundColor(color);
//        parallaxScrollView.setParallaxOverLayColor(color);
//        parallaxScrollView.setBackgroundColor(color);
        mForm.setIconTintColor(color);
        //findViewById(R.id.parallax_view).setBackgroundColor(color);
        findViewById(R.id.parallax_view_edit).setBackgroundColor(color);
        // findViewById(R.id.categoryScrollViewEdit).setBackgroundColor(color);
        if (captureImage != null) {
            GradientDrawable shapeDrawable =
                    (GradientDrawable) getResources().getDrawable(R.drawable.circle_mask_primary);
            shapeDrawable.setColor(color);
            captureImage.setBackgroundDrawable(shapeDrawable);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_category_save:
//                OValues values = mForm.getValues();
//                if (values != null) {
//                    if (newImage != null) {
//                        values.put("image_small", newImage);
//                        values.put("large_image", newImage);
//                    }
//                    if (record != null) {
//                        posCategory.update(record.getInt(OColumn.ROW_ID), values);
//                        Toast.makeText(this, R.string.toast_information_saved, Toast.LENGTH_LONG).show();
//                        mEditMode = !mEditMode;
//                        setupActionBar();
//                    } else {
//                        //values.put("name", OVarchar.class);
//                        final int row_id = posCategory.insert(values);
//                        if (row_id != OModel.INVALID_ROW_ID) {
//                            finish();
//                        }
//                    }
//                    Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
//                            Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.menu_category_cancel:
//                if (record == null) {
//                    finish();
//                    return true;
//
//                }
//            case R.id.menu_category_edit:
//                mEditMode = !mEditMode;
//                setMode(mEditMode);
//                mForm.setEditable(mEditMode);
//                mForm.initForm(record);
//                setCustomerImage();
//                break;
//            case R.id.menu_category_share:
//                ShareUtil.shareContact(this, record, true);
//                break;
//            case R.id.menu_category_import:
//                ShareUtil.shareContact(this, record, false);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_category_detail, menu);
        mMenu = menu;
        setMode(mEditMode);
        return true;
}

    @Override
    public void onFieldValueChange(OField field, Object value) {
//        if (field.getFieldName().equals("is_company")) {
//            Boolean checked = Boolean.parseBoolean(value.toString());
//            int view = (checked) ? View.GONE : View.VISIBLE;
//            findViewById(R.id.parent_id).setVisibility(view);
//
//        }
////        Toast.makeText(this," Select Name Category", Toast.LENGTH_LONG).show();
////        if (field.getFieldName().equals("parent_id")) {
////            Toast.makeText(this, "Changed selected value", Toast.LENGTH_LONG).show();
//
//        }
    }


    private class BigImageLoader extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String image = null;
            try {
                Thread.sleep(300);
                OdooFields fields = new OdooFields();
                fields.addAll(new String[]{"image_medium"});
                OdooRecord record = resPartner.getServerDataHelper().read(null, params[0]);
                if (!record.getString("image_medium").equals("false")) {
                    image = record.getString("image_medium");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                if (!result.equals("false")) {
                    OValues values = new OValues();
                    values.put("large_image", result);
                    resPartner.update(record.getInt(OColumn.ROW_ID), values);
                    record.put("large_image", result);
                    setCustomerImage();
                }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_MODE, mEditMode);
        outState.putString(KEY_NEW_IMAGE, newImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OValues values = fileManager.handleResult(requestCode, resultCode, data);
        if (values != null && !values.contains("size_limit_exceed")) {
            newImage = values.getString("datas");
            userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            userImage.setColorFilter(null);
            userImage.setImageBitmap(BitmapUtils.getBitmapImage(this, newImage));
        } else if (values != null) {
            Toast.makeText(this, R.string.toast_image_size_too_large, Toast.LENGTH_LONG).show();
        }
    }
}

