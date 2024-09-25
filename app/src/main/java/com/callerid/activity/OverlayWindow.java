package com.callerid.activity;

import static android.content.Context.WINDOW_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.callerid.R;
import com.callerid.adapter.ActivityAdapter;
import com.callerid.adapter.ContentAdapter;
import com.callerid.adapter.EditStatusAdapter;
import com.callerid.adapter.EditlabelAdapter;
import com.callerid.adapter.NoteAdapter;
import com.callerid.adapter.TaskAdapter;
import com.callerid.model.ActivityModel;
import com.callerid.model.ActivityResponseModel;
import com.callerid.model.CallModel;
import com.callerid.model.ContentModel;
import com.callerid.model.ContentResponseModel;
import com.callerid.model.ExtraModel;
import com.callerid.model.LabelModel;
import com.callerid.model.LinkRequestModel;
import com.callerid.model.NoteModel;
import com.callerid.model.NoteResponseModel;
import com.callerid.model.ObjectResponseModel;
import com.callerid.model.RequestModel;
import com.callerid.model.ResponseModel;
import com.callerid.model.SimpleResponseModel;
import com.callerid.model.StatusModel;
import com.callerid.model.TaskModel;
import com.callerid.model.TaskResponseModel;
import com.callerid.model.UploadImage;
import com.callerid.service.OverlayService;
import com.callerid.utils.NetworkUtil;
import com.callerid.utils.RetroFit;
import com.callerid.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.sqlite.AsSqlLite;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressLint("InflateParams, ClickableViewAccessibility")
public class OverlayWindow<lytEdit> {
    private final Context context;

    private CompositeDisposable disposable;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private ImageView fabCall;
    private ConstraintLayout layCons, layPopup, layFab, consl;
    private AppCompatImageView icCall, icWhatsapp, icSettings, icClose, icOption, icCloseButton, icShareButton, icCallTypePopUp;
    private AppCompatTextView txCallType, txCallTopType, txCallerName, textPopup, txCallUserName, txCallerNo, txCallUserNumber, tvNotes, txIntegrationName, txEditStatus, txStatus, txTimeline, txTask, txNotes, txInfo;
    private ChipGroup chipInfo, chipStatus, chipPopUp;
    private ShimmerFrameLayout shimmerDymanicLayout;
    private LinearLayout lytEdit;
    private SpinKitView progress;
    private TextView txtEditStatus, txtEditLabel;
    private RecyclerView rvEditStatusList, rvEditLabelList;
    private View bottom_sheet_edit_status, bottom_sheet_edit_label;
    private View bottom_sheet_dont_show;
    private List<AppCompatTextView> txView;
    private final static int[] icIds = {R.drawable.ic_timeline, R.drawable.ic_task, R.drawable.ic_notes, R.drawable.ic_content};
    private final static int[] icActiveIds = {R.drawable.ic_timeline_active, R.drawable.ic_task_active, R.drawable.ic_notes_primary, R.drawable.ic_content_active};
    private static int curScreen = 0;
    private final CallModel callModel;
    private static String mobileNumber, unknown = "Unknown";
    private static Long curAction = 0L, lastAction = 0L;
    boolean showfull = false;
    ConstraintLayout laychange;
    String callType = "";
    String fileName = "";
    com.google.android.material.card.MaterialCardView bar;

    public OverlayWindow(Context context, CallModel callModel) {
        this.context = context;
        this.callModel = callModel;
        disposable = new CompositeDisposable();
        mobileNumber = null;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        setView();
    }

    public OverlayWindow(Context context, CallModel callModel, boolean big) {
        this.context = context;
        this.callModel = callModel;
        disposable = new CompositeDisposable();
        mobileNumber = null;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        showfull = big;
        setView();
    }

    WindowManager mWindowManager;
    private ConstraintLayout basictab;
    private RelativeLayout tasklay;
    private TextView addtask, txtLabelTimeline;
    private RelativeLayout addtasklay, rlStatus, rlLabel;
    private ChipGroup chiptasktype;
    private LinearLayout datetimelay, llCrateFlowUpTask;
    private DatePicker dp_datepicker;
    private TimePicker tp_timepicker;
    private AppCompatButton savetask;
    private AppCompatButton btnDontShow;
    private AppCompatButton btnBack, btnOk, btnBackStatus;
    private ConstraintLayout tasklistview;
    private RecyclerView task_views;
    private RelativeLayout notelay;
    private TextView addnote;
    private RelativeLayout addnotelay, lytAddNotes;
    private EditText noteDesc;
    private AppCompatButton savenote;
    private ConstraintLayout notelistview;
    private RecyclerView note_views;
    TextView success;
    RecyclerView content_views;
    RelativeLayout contentlay;
    ContentAdapter contentAdapter;
    RecyclerView activity_views;
    ChipGroup chipRemind;
    private Handler handler;
    private Runnable dismissDialogRunnable;
    String[] courses = {"All", "File", "Page", "Message", "dlt_sms"};
    TaskAdapter taskAdapter;
    NoteAdapter noteAdapter;
    EditStatusAdapter editStatusAdapter;
    EditlabelAdapter editlabelAdapter;
    FrameLayout rootframe;
    ArrayList<TaskModel> tasklist = new ArrayList<>();
    ArrayList<NoteModel> notelist = new ArrayList<>();
    ArrayList<String> labelList = new ArrayList<>();
    TextView nulllay,EmptyMessageForTask,EmptyMessageForTime;
    Spinner contentSpinner;

    ArrayList<ContentModel> contentlist = new ArrayList<>();

    ArrayList<ActivityModel> activitylist = new ArrayList<>();
    ActivityAdapter activityAdapter;
    AppCompatButton backnote, backtask;
    String DEEP_LINK_URL = "";
    String currentTime = "";
    String leadIds = "";
    AsSqlLite asSqlLite;
    String mCallType = "";

    void callrest(View view) {
        try {
            fabCall = view.findViewById(R.id.fabCall);
            icClose = view.findViewById(R.id.icClose);
            icCloseButton = view.findViewById(R.id.icCloseButton);
            icShareButton = view.findViewById(R.id.icShareButton);
            icOption = view.findViewById(R.id.icOption);
            view.setFitsSystemWindows(false); // allow us to draw over status bar, navigation bar


            layCons = view.findViewById(R.id.layCons);
            layPopup = view.findViewById(R.id.layPopup);
            consl = view.findViewById(R.id.consl);
            layFab = view.findViewById(R.id.layFab);
            shimmerDymanicLayout = view.findViewById(R.id.shimmerDymanicLayout);
            laychange = view.findViewById(R.id.layChange);
            bar = view.findViewById(R.id.bar);
            chipRemind = view.findViewById(R.id.chipRemind);
            icCall = view.findViewById(R.id.icCall);
            icWhatsapp = view.findViewById(R.id.icWhatsapp);
            icCallTypePopUp = view.findViewById(R.id.icCallTypePopUp);
            icSettings = view.findViewById(R.id.icSettings);
            backnote = (AppCompatButton) view.findViewById(R.id.backnote);
            backtask = (AppCompatButton) view.findViewById(R.id.backtask);
            txCallType = view.findViewById(R.id.txCallType);
            rlStatus = (RelativeLayout) view.findViewById(R.id.rlStatus);
            rlLabel = (RelativeLayout) view.findViewById(R.id.rlLabel);
            txCallTopType = view.findViewById(R.id.txCallTopType);
            txCallerName = view.findViewById(R.id.txCallerName);
            txCallUserName = view.findViewById(R.id.txCallUserName);
            txCallerNo = view.findViewById(R.id.txCallerNo);
            tvNotes = view.findViewById(R.id.tvNotes);
            txCallUserNumber = view.findViewById(R.id.txCallUserNumber);
            textPopup = view.findViewById(R.id.textPopup);
            txIntegrationName = view.findViewById(R.id.txIntegrationName);
            txEditStatus = view.findViewById(R.id.txEditStatus);
            txStatus = view.findViewById(R.id.txStatus);
            lytEdit = view.findViewById(R.id.lytEdit);
            txtEditStatus = view.findViewById(R.id.txtEditStatus);
            rvEditStatusList = view.findViewById(R.id.rvEditStatusList);
            rvEditLabelList = view.findViewById(R.id.rvEditLabelList);
            bottom_sheet_edit_status = view.findViewById(R.id.bottom_sheet_edit_status);
            bottom_sheet_edit_label = view.findViewById(R.id.bottom_sheet_edit_label);
            txtEditLabel = view.findViewById(R.id.txtEditLabel);
            success = view.findViewById(R.id.success);
            txTimeline = view.findViewById(R.id.txTimeline);
            txTask = view.findViewById(R.id.txTask);
            txNotes = view.findViewById(R.id.txNotes);
            txInfo = view.findViewById(R.id.txInfo);
            nulllay = view.findViewById(R.id.nulllay);
            EmptyMessageForTask = view.findViewById(R.id.EmptyMessageForTask);
            EmptyMessageForTime = view.findViewById(R.id.EmptyMessageForTime);
            chipInfo = view.findViewById(R.id.chipInfo);
            chipPopUp = view.findViewById(R.id.chip);
//            chipStatus = view.findViewById(R.id.chipStatus);
            progress = view.findViewById(R.id.progressBar);
            nulllay.setVisibility(View.GONE);
            basictab = (ConstraintLayout) view.findViewById(R.id.basictab);
            txtLabelTimeline = (TextView) view.findViewById(R.id.txtLabelTimeline);
            tasklay = (RelativeLayout) view.findViewById(R.id.tasklay);
            addtask = (TextView) view.findViewById(R.id.addtask);
            addtasklay = (RelativeLayout) view.findViewById(R.id.addtasklay);
            chiptasktype = (ChipGroup) view.findViewById(R.id.chiptasktype);
            datetimelay = (LinearLayout) view.findViewById(R.id.datetimelay);
            dp_datepicker = (DatePicker) view.findViewById(R.id.dp_datepicker);
            tp_timepicker = (TimePicker) view.findViewById(R.id.tp_timepicker);
            savetask = (AppCompatButton) view.findViewById(R.id.savetask);
            btnDontShow = (AppCompatButton) view.findViewById(R.id.btnDontShow);
            btnBack = (AppCompatButton) view.findViewById(R.id.btnBack);
            btnOk = (AppCompatButton) view.findViewById(R.id.btnOk);
            btnBackStatus = (AppCompatButton) view.findViewById(R.id.btnBackStatus);
            tasklistview = (ConstraintLayout) view.findViewById(R.id.tasklistview);
            task_views = (RecyclerView) view.findViewById(R.id.task_views);
            notelay = (RelativeLayout) view.findViewById(R.id.notelay);
            addnote = (TextView) view.findViewById(R.id.addnote);
            addnotelay = (RelativeLayout) view.findViewById(R.id.addnotelay);
            lytAddNotes = (RelativeLayout) view.findViewById(R.id.lytAddNotes);
            noteDesc = (EditText) view.findViewById(R.id.noteDesc);
            savenote = (AppCompatButton) view.findViewById(R.id.savenote);
            notelistview = (ConstraintLayout) view.findViewById(R.id.notelistview);
            note_views = (RecyclerView) view.findViewById(R.id.note_views);
            contentlay = view.findViewById(R.id.contentlay);
            content_views = view.findViewById(R.id.content_views);

            activity_views = view.findViewById(R.id.activity_views);
            activityAdapter = new ActivityAdapter(context, activitylist);
            activity_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            activity_views.setAdapter(activityAdapter);
            contentAdapter = new ContentAdapter(context, contentlist, new ContentAdapter.ClickListener() {
                @Override
                public void onWhatsappClick(String text) {
                    whatsapp(text);
                }

                @Override
                public void onMessageClick(String text) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    text = text.replace("@lead name", cm.getName());
                    intent.setData(Uri.parse("smsto:" + Uri.encode(mobileNumber)));
                    intent.putExtra("sms_body", text);
                    context.startActivity(intent);
                    close();
                }

                @Override
                public void onGenerateLink(LinkRequestModel model, boolean isWhatsapp, String title) {
                    Getlink(model, isWhatsapp, title);
                }
            });
            content_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            content_views.setAdapter(contentAdapter);
            tp_timepicker.setIs24HourView(true);

            txtLabelTimeline.setVisibility(View.GONE);
            basictab.setVisibility(View.VISIBLE);
            tasklay.setVisibility(View.GONE);
            notelay.setVisibility(View.GONE);
            contentlay.setVisibility(View.GONE);

            txView = new ArrayList<>();
            txView.add(txTimeline);
            txView.add(txTask);
            txView.add(txNotes);
            txView.add(txInfo);

//        layoutParams.gravity = Gravity.BOTTOM;

            //   view.findViewById(R.id.layChange).setOnClickListener(null);
            //  view.findViewById(R.id.layCons).setOnClickListener(v -> close());
            icClose.setOnClickListener(v -> close());

            icOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDismissHandler();
                    if (btnDontShow.getVisibility() == View.VISIBLE) {
                        btnDontShow.setVisibility(View.GONE);
                    } else btnDontShow.setVisibility(View.VISIBLE);

                }
            });

            icCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideFab();
                }
            });

            //   fabCall.setOnClickListener(v -> showFabPopup());
      /*  layPopup.setOnClickListener(v -> {
            curAction = System.currentTimeMillis();
            if (curAction - lastAction < 300) showFabPopup();
            lastAction = curAction;
        });
*/

            icCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    call();
                    resetDismissHandler();
                }
            });
            icWhatsapp.setOnClickListener(v -> whatsapp());
            icWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    whatsapp();
                    resetDismissHandler();
                }
            });
            icShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDismissHandler();
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse(DEEP_LINK_URL)));
                        close();
                    } catch (Exception e) {

                        showSuccess("CRM app not installed on this device");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                                close();
                            }
                        }, 2500);

                    }
                }
            });


            txTimeline.setOnClickListener(v -> txActive(0));
            txTimeline.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });
            txTask.setOnClickListener(v -> txActive(1));
            txTask.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });
            txNotes.setOnClickListener(v -> txActive(2));
            txNotes.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });
            txInfo.setOnClickListener(v -> txActive(3));
            txInfo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });

            addtask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDismissHandler();
                    task_views.setVisibility(addtasklay.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                    addtasklay.setVisibility(addtasklay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    nulllay.setVisibility(View.GONE);

                    List<LabelModel> lm = new ArrayList<>();
                    lm.add(new LabelModel("Call", "#111111", "as"));
                    lm.add(new LabelModel("Follow-Up", "#111111", "as"));
                    lm.add(new LabelModel("Meeting", "#111111", "as"));

                    addTaskChip(lm);

                }
            });

            addnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDismissHandler();
                    note_views.setVisibility(addnotelay.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                    addnotelay.setVisibility(addnotelay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    addnotecalled = true;
                    nulllay.setVisibility(View.GONE);


                }
            });
            notelay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });

            if (backnote != null) {
                backnote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetDismissHandler();
                        note_views.setVisibility(View.VISIBLE);
                        addnotecalled = false;
                        addnotelay.setVisibility(View.GONE);
                        //callNote(cm);
                    }
                });
            } else {
                Log.e("OverlayWindow", "Button is null");
            }

            if (backtask != null) {
                backtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetDismissHandler();
                        task_views.setVisibility(View.VISIBLE);
                        callTask(cm);
                        addtasklay.setVisibility(View.GONE);
                    }
                });
            } else {
                Log.e("OverlayWindow", "Button is null");
            }


            contentSpinner = view.findViewById(R.id.contentSpinner);

            ArrayAdapter ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, courses);

            // set simple layout resource file
            // for each item of spinner
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set the ArrayAdapter (ad) data on the
            // Spinner which binds data to spinner
            contentSpinner.setAdapter(ad);

            contentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    contentlist.clear();
                    contentAdapter.notifyDataSetChanged();
                    String val = courses[position];
                    callcontent(val);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            icSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDismissHandler();
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse(DEEP_LINK_URL)));
                        close();
                    } catch (Exception e) {

                        showSuccess("CRM app not installed on this device");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                                close();
                            }
                        }, 2500);

                    }
                }
            });


            setData(callModel);

            btnDontShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDismissHandler();
                    callDowntShow();

                }
            });

            savetask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDismissHandler();
                    saveTask();
                    task_views.setVisibility(View.VISIBLE);

                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDismissHandler();
                    bottom_sheet_edit_label.setVisibility(View.GONE);
                    consl.setVisibility(View.VISIBLE);
                }
            });

            btnBackStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDismissHandler();
                    bottom_sheet_edit_status.setVisibility(View.GONE);
                    consl.setVisibility(View.VISIBLE);
                }
            });


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDismissHandler();
                    bottom_sheet_edit_label.setVisibility(View.GONE);
                    consl.setVisibility(View.VISIBLE);

                    callUpdateLabel(labelList);
                }
            });
            bar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });

            boolean result = isTimeBetween12amAnd5pm();
            boolean isWeekend = isWeekend();
            Chip twoH = view.findViewById(R.id.twohr);
            Chip tmrw = view.findViewById(R.id.tmrw);
            Chip mondayChip = view.findViewById(R.id.mondayChip);
            Chip tuesdayChip = view.findViewById(R.id.tuesdayChip);
            if (result) {
                twoH.setText("5 PM");
                tmrw.setText("Tomorrow 10 AM");
            } else {
                twoH.setText("11 AM");
                tmrw.setText("Tomorrow");
            }

            if (isWeekend) {
                mondayChip.setText("Monday");
                tuesdayChip.setText("TuesdayChip");
            } else {
                mondayChip.setText("Friday");
                tuesdayChip.setText("Next Week");
            }

            chipRemind.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup group, int checkedId) {
                    resetDismissHandler();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    String newTime = "";
                    switch (checkedId) {
                        case R.id.fivemin:
                            cal.add(Calendar.MINUTE, 30);
                            newTime = df.format(cal.getTime());

                            break;
                        case R.id.thirtymin:
                            cal.add(Calendar.HOUR, 1);
                            newTime = df.format(cal.getTime());

                            break;

                        case R.id.twohr:
                            if (result) {
                                cal.add(Calendar.HOUR, 17);
                                newTime = df.format(cal.getTime());
                            } else {
                                cal.add(Calendar.DAY_OF_MONTH, 1); // Move to tomorrow
                                cal.set(Calendar.HOUR_OF_DAY, 11);
                                newTime = df.format(cal.getTime());
                            }

                            break;

                        case R.id.tmrw:

                            if (result) {
                                cal.add(Calendar.DAY_OF_YEAR, 1); // Move to the next day
                                cal.add(Calendar.HOUR_OF_DAY, 10);
                                newTime = df.format(cal.getTime());
                            } else {
                                cal.add(Calendar.DAY_OF_YEAR, 1);
                                newTime = df.format(cal.getTime());
                            }

                        case R.id.mondayChip:

                            if (isWeekend) {
                                int today = cal.get(Calendar.DAY_OF_WEEK);
                                int daysUntilMonday = (Calendar.MONDAY - today + 7) % 7;
                                if (daysUntilMonday == 0) {
                                    daysUntilMonday = 7; // Skip the current week if it's already Monday
                                }
                                cal.add(Calendar.DAY_OF_MONTH, daysUntilMonday);

                                // Set to start of the day
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                            } else {
                                int today = cal.get(Calendar.DAY_OF_WEEK);
                                int daysUntilFriday = (Calendar.FRIDAY - today + 7) % 7;
                                if (daysUntilFriday == 0) {
                                    daysUntilFriday = 7; // Move to the next week if today is Friday
                                }
                                cal.add(Calendar.DAY_OF_MONTH, daysUntilFriday);
                                cal.set(Calendar.HOUR_OF_DAY, 11); // 11 AM
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);

                                // Add 30 minutes
                                cal.add(Calendar.MINUTE, 30);
                                newTime = df.format(cal.getTime());
                            }

                            break;

                        case R.id.tuesdayChip:

                            if (isWeekend) {
                                int today = cal.get(Calendar.DAY_OF_WEEK);
                                int daysUntilTuesday = (Calendar.TUESDAY - today + 7) % 7;
                                if (daysUntilTuesday == 0) {
                                    daysUntilTuesday = 7; // Skip the current week if it's already Tuesday
                                }
                                cal.add(Calendar.DAY_OF_MONTH, daysUntilTuesday);

                                // Set to start of the day
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                            } else {
                                cal.add(Calendar.DAY_OF_YEAR, 7);
                                newTime = df.format(cal.getTime());
                            }

                            break;

                        case R.id.nextFridayChip:

                            if (isWeekend) {
                                int today = cal.get(Calendar.DAY_OF_WEEK);
                                int daysUntilFriday = (Calendar.FRIDAY - today + 7) % 7;
                                if (daysUntilFriday == 0) {
                                    daysUntilFriday = 7; // Skip the current week if it's already Friday
                                }
                                cal.add(Calendar.DAY_OF_MONTH, daysUntilFriday);

                                // Set to start of the day
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                            } else {
                                int today = cal.get(Calendar.DAY_OF_WEEK);
                                int daysUntilFriday = (Calendar.FRIDAY - today + 7) % 7;
                                if (daysUntilFriday == 0) {
                                    daysUntilFriday = 7; // Skip the current week if it's already Friday
                                }
                                cal.add(Calendar.DAY_OF_MONTH, daysUntilFriday);

                                // Set to start of the day
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                newTime = df.format(cal.getTime());
                            }

                            break;

                        default:
                            return;

                    }

                    saveInstantTask(newTime);

                }
            });
            chipRemind.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });

            if (showfull) {
//                if (NetworkUtil.isNetworkConnected(context) && cm != null) {
//                    showPopup();
//                }
            }

            //showSuccess("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isWeekend() {
        // Get the current day of the week
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);

        // Check if today is Friday, Saturday, or Sunday
        return today == Calendar.FRIDAY || today == Calendar.SATURDAY || today == Calendar.SUNDAY;
    }

    public static boolean isTimeBetween12amAnd5pm() {
        // Get the current time
        Calendar now = Calendar.getInstance();

        // Define the start and end times
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0); // 12 AM
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 17); // 5 PM
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);

        // Check if the current time is between start and end times
        return now.after(startTime) && now.before(endTime);
    }

    private void callDowntShow() {

        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).addDontShow("" + mobileNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SimpleResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull SimpleResponseModel rm) {
                        hideProgress();

                        if (rm.isSuccess()) {

                            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                            btnDontShow.setVisibility(View.GONE);

                            asSqlLite.insertIgnoreList("", "", mobileNumber, "");

                        } else {
                            showSuccess("Couldn't add task");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }

    }


    private void setView() {
        asSqlLite = new AsSqlLite(context);
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getWindowManagerDefaultDisplay();


        addRemoveView(layoutInflater);

        addFloatingWidgetView(layoutInflater);
        implementClickListeners();

        addViewOnTouchListener();

    }


    ResponseModel.Data cm;

    private void implementClickListeners() {

        mFloatingWidgetView.findViewById(R.id.close_expanded_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFab();
            }
        });

        mFloatingWidgetView.findViewById(R.id.rlStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDismissHandler();
                setEditStatus(cm.getUserPreference().getUser_status());
                chipRemind.setVisibility(View.GONE);
                txStatus.setVisibility(View.GONE);
                bottom_sheet_edit_status.setVisibility(View.VISIBLE);
                consl.setVisibility(View.GONE);
            }
        });


        mFloatingWidgetView.findViewById(R.id.rlLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDismissHandler();
                setEditLabel(cm.getUserPreference().getUser_Label());
                chipRemind.setVisibility(View.GONE);
                txStatus.setVisibility(View.GONE);
                labelList.clear();
                bottom_sheet_edit_label.setVisibility(View.VISIBLE);
                consl.setVisibility(View.GONE);
            }
        });

        mFloatingWidgetView.findViewById(R.id.tvNotInterested).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDesc.setText("Not interested");
            }
        });
        mFloatingWidgetView.findViewById(R.id.tvFollowingUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDesc.setText("Following up");

            }
        });
        mFloatingWidgetView.findViewById(R.id.tvDidNotPick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDesc.setText("Did not pick");
            }
        });
        mFloatingWidgetView.findViewById(R.id.tvCallBackLater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDesc.setText("Call back later");
            }
        });
        mFloatingWidgetView.findViewById(R.id.savenote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                note_views.setVisibility(View.VISIBLE);

            }
        });


        mFloatingWidgetView.findViewById(R.id.addtask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_views.setVisibility(addtasklay.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                addtasklay.setVisibility(addtasklay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                nulllay.setVisibility(View.GONE);

                List<LabelModel> lm = new ArrayList<>();
                lm.add(new LabelModel("Call", "#111111", "as"));
                lm.add(new LabelModel("Follow-Up", "#111111", "as"));
                lm.add(new LabelModel("Meeting", "#111111", "as"));

                addTaskChip(lm);

            }
        });

        mFloatingWidgetView.findViewById(R.id.addnote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_views.setVisibility(addnotelay.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                addnotelay.setVisibility(addnotelay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                addnotecalled = true;
                nulllay.setVisibility(View.GONE);


            }
        });

//        backnote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                note_views.setVisibility(View.VISIBLE);
//                addnotecalled = false;
//                addnotelay.setVisibility(View.GONE);
//                //callNote(cm);
//            }
//        });
//        backtask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                task_views.setVisibility(View.VISIBLE);
//                callTask(cm);
//                addtasklay.setVisibility(View.GONE);
//            }
//        });
//
//
//        addnote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                note_views.setVisibility(addnotelay.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
//                addnotelay.setVisibility(addnotelay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//
//                addnotecalled = true;
//                nulllay.setVisibility(View.GONE);
//
//
//            }
//        });
//
//
//        icCall.setOnClickListener(v -> call());
//        icWhatsapp.setOnClickListener(v -> whatsapp());
//        icShareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse(DEEP_LINK_URL)));
//                    close();
//                } catch (Exception e) {
//
//                    showSuccess("CRM app not installed on this device");
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideSuccess();
//                            close();
//                        }
//                    }, 2500);
//
//                }
//            }
//        });
//
//
//        mFloatingWidgetView.findViewById(R.id.txTimeline).setOnClickListener(v -> txActive(0));
//        mFloatingWidgetView.findViewById(R.id.txTask).setOnClickListener(v -> txActive(1));
//        mFloatingWidgetView.findViewById(R.id.txNotes).setOnClickListener(v -> txActive(2));
//        mFloatingWidgetView.findViewById(R.id.txInfo).setOnClickListener(v -> txActive(3));
//        mFloatingWidgetView.findViewById(R.id.icClose).setOnClickListener(v -> close());
//
//        mFloatingWidgetView.findViewById(R.id.icOption).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (btnDontShow.getVisibility() == View.VISIBLE) {
//                    btnDontShow.setVisibility(View.GONE);
//                } else btnDontShow.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        mFloatingWidgetView.findViewById(R.id.icCloseButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideFab();
//            }
//        });
//
//        mFloatingWidgetView.findViewById(R.id.icSettings).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse(DEEP_LINK_URL)));
//                    close();
//                } catch (Exception e) {
//
//                    showSuccess("CRM app not installed on this device");
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideSuccess();
//                            close();
//                        }
//                    }, 2500);
//
//                }
//            }
//        });
//
//
//        mFloatingWidgetView.findViewById(R.id.btnDontShow).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callDowntShow();
//
//            }
//        });
//
//        mFloatingWidgetView.findViewById(R.id.savetask).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveTask();
//                task_views.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        mFloatingWidgetView.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottom_sheet_edit_label.setVisibility(View.GONE);
//                consl.setVisibility(View.VISIBLE);
//            }
//        });
//
//        mFloatingWidgetView.findViewById(R.id.btnBackStatus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottom_sheet_edit_status.setVisibility(View.GONE);
//                consl.setVisibility(View.VISIBLE);
//            }
//        });
//
//
//        mFloatingWidgetView.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottom_sheet_edit_label.setVisibility(View.GONE);
//                consl.setVisibility(View.VISIBLE);
//
//                callUpdateLabel(labelList);
//            }
//        });

    }

    private View addRemoveView(LayoutInflater inflater) {
        //Inflate the removing view layout we created
        removeFloatingWidgetView = inflater.inflate(R.layout.remove_floating_widget_layout, null);

        //Add the view to the window.
        WindowManager.LayoutParams paramRemove;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            paramRemove = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

        } else {
            paramRemove = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
        }
        //Specify the view position
        // paramRemove.gravity = Gravity.TOP | Gravity.LEFT;
        paramRemove.y = szWindow.y - (removeFloatingWidgetView.getHeight() + getStatusBarHeight());
        //Initially the Removing widget view is not visible, so set visibility to GONE
        removeFloatingWidgetView.setVisibility(View.GONE);
        remove_image_view = (ImageView) removeFloatingWidgetView.findViewById(R.id.remove_img);

        remove_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFloatingWidgetView.setVisibility(View.GONE);
            }
        });
        //Add the view to the window
        mWindowManager.addView(removeFloatingWidgetView, paramRemove);
        return remove_image_view;
    }

    private ImageView remove_image_view;
    private Point szWindow = new Point();
    private View removeFloatingWidgetView;
    private int windowHeight;
    private int windowWidth;
    WindowManager.LayoutParams floatparams;

    private View mFloatingWidgetView, collapsedView, expandedView;


    private void addFloatingWidgetView(LayoutInflater inflater) {
        //Inflate the floating view layout we created
        mFloatingWidgetView = inflater.cloneInContext(new ContextThemeWrapper(context, R.style.CalledIdTheme)).inflate(R.layout.lay_caller_id, null);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            floatparams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,  // Changed to WRAP_CONTENT for width
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        } else {
            floatparams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,  // Changed to WRAP_CONTENT for width
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;

// Specify the view position
        floatparams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL; // Center horizontally with margin on both sides

        mWindowManager.addView(mFloatingWidgetView, floatparams);

// Find id of collapsed view layout
        collapsedView = mFloatingWidgetView.findViewById(R.id.collapse_view);

// Find id of the expanded view layout
        expandedView = mFloatingWidgetView.findViewById(R.id.expanded_container);
        rootframe = mFloatingWidgetView.findViewById(R.id.rootframe);

        callrest(mFloatingWidgetView);
    }

    private void getWindowManagerDefaultDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            mWindowManager.getDefaultDisplay().getSize(szWindow);
        else {
            int w = mWindowManager.getDefaultDisplay().getWidth();
            int h = mWindowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }
    }

    /*  on Floating Widget Long Click, increase the size of remove view as it look like taking focus */
    private void onFloatingWidgetLongClick() {
        //Get remove Floating view params
        WindowManager.LayoutParams removeParams = (WindowManager.LayoutParams) removeFloatingWidgetView.getLayoutParams();

        //get x and y coordinates of remove view
        int x_cord = (szWindow.x - removeFloatingWidgetView.getWidth()) / 2;
        int y_cord = szWindow.y - (removeFloatingWidgetView.getHeight() + getStatusBarHeight());


        removeParams.x = x_cord;
        removeParams.y = y_cord;

        //Update Remove view params
        mWindowManager.updateViewLayout(removeFloatingWidgetView, removeParams);
    }

    private int getStatusBarHeight() {
        return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

    /*  Detect if the floating view is collapsed or expanded */
    private boolean isViewCollapsed() {
        return mFloatingWidgetView == null || mFloatingWidgetView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    /*  on Floating widget click show expanded view  */
    private void onFloatingWidgetClick() {
        if (isViewCollapsed()) {
            //When user clicks on the image view of the collapsed layout,
            //visibility of the collapsed layout will be changed to "View.GONE"
            //and expanded view will become visible.
//            if (NetworkUtil.isNetworkConnected(context) && cm != null) {
//                showPopup();
//            }

        } else {
            //  showFab();
            close();
        }
    }

    private void addViewOnTouchListener() {

        if (mFloatingWidgetView != null) {
            mFloatingWidgetView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_OUTSIDE:
//                            showFab();

                            break;
                    }
                    return false;
                }
            });

            View rootContainer = mFloatingWidgetView.findViewById(R.id.root_container);
            if (rootContainer != null) {
                rootContainer.setOnTouchListener(new View.OnTouchListener() {

                    private int initialX;
                    private int initialY;

                    long time_start = 0, time_end = 0;


                    int remove_img_width = 0, remove_img_height = 0;
                    boolean isLongClick = false;//variable to judge if user click long press
                    private float initialTouchX;
                    private float initialTouchY;
                    Handler handler_longClick = new Handler();
                    Runnable runnable_longClick = new Runnable() {
                        @Override
                        public void run() {
                            //On Floating Widget Long Click

                            //Set isLongClick as true
                            isLongClick = true;

                            //Set remove widget view visibility to VISIBLE
                            removeFloatingWidgetView.setVisibility(View.VISIBLE);

                            onFloatingWidgetLongClick();
                        }
                    };

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        Log.e("rootContainer", "root_container is onTouch. ");
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                time_start = System.currentTimeMillis();

                                handler_longClick.postDelayed(runnable_longClick, 300);

                                remove_img_width = remove_image_view.getLayoutParams().width;
                                remove_img_height = remove_image_view.getLayoutParams().height;

                                initialX = floatparams.x;
                                initialY = floatparams.y;
                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();
                                // add closeview when moving video view
                                onFloatingWidgetLongClick();

                                return true;
                            case MotionEvent.ACTION_UP:
                                if (mFloatingWidgetView.getWindowToken() != null) {
                                    isLongClick = false;
                                    removeFloatingWidgetView.setVisibility(View.GONE);
                                    remove_image_view.getLayoutParams().height = remove_img_height;
                                    remove_image_view.getLayoutParams().width = remove_img_width;
                                    handler_longClick.removeCallbacks(runnable_longClick);
                                    if ((Math.abs(initialTouchX - event.getRawX()) < 5) && (Math.abs(initialTouchY - event.getRawY()) < 5)) {

                                        time_end = System.currentTimeMillis();

                                        //Also check the difference between start time and end time should be less than 300ms
                                        if ((time_end - time_start) < 300) onFloatingWidgetClick();

                                    }

                                    int centerOfScreenByX = windowWidth / 2;

                                    // remove video view when the it is in the close view area
                                    if ((floatparams.y > windowHeight - removeFloatingWidgetView.getHeight() - removeFloatingWidgetView.getHeight()) && ((floatparams.x > centerOfScreenByX - remove_image_view.getWidth() - mFloatingWidgetView.getWidth()) && (floatparams.x < centerOfScreenByX + remove_image_view.getWidth()))) {


                                        close();
                                    } else {
                                        moveToRight(floatparams.x);
                                    }


                                    // always remove close view ImageView when video view is dropped
                                    removeFloatingWidgetView.setVisibility(View.GONE);
                                    //ivCloseView = null;
                                }
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                // move videoview ImageView

                                floatparams.x = initialX + (int) (initialTouchX - event.getRawX());
                                floatparams.y = initialY + (int) (event.getRawY() - initialTouchY);
                                mWindowManager.updateViewLayout(mFloatingWidgetView, floatparams);

                                return true;
                        }
                        return false;
                    }
                    // The rest of your onTouch implementation...
                });
            } else {
                Log.e("Error", "root_container is null. Check layout inflation.");
            }

            addnotelay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });

            lytAddNotes.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });
            notelistview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });

            note_views.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    resetDismissHandler();
                    return false;
                }
            });
        }


    }


    void Getlink(LinkRequestModel rm, boolean isWhatsapp, String title) {
        ArrayList<String> leadlist = new ArrayList<>();
        if (cm != null && cm.get_id() != null) {
            leadlist.add(cm.get_id());
            rm.setLead_ids(leadlist);
        }
        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).GetLink(rm).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<ObjectResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull ObjectResponseModel rm) {
                        hideProgress();
                        if (rm.isSuccess()) {

                            ContentModel contentModel = rm.getData();
                            String text = "";
                            if (cm != null) {
                                text = "Dear " + cm.getName() + "\n here is your link to view " + title;
                                text = text + "\n" + contentModel.getUniqueLink();
                            } else {
                                text = "\n here is your link to view " + title;
                                text = text + "\n" + contentModel.getUniqueLink();
                            }
                            if (isWhatsapp) {
                                whatsapp(text);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                intent.setData(Uri.parse("smsto:" + Uri.encode(mobileNumber)));
                                intent.putExtra("sms_body", text);
                                context.startActivity(intent);
                                close();
                            }
                            // Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                            //    callTask(cm);

                        } else {
                            //  Toast.makeText(context,"Couldn't add task",Toast.LENGTH_LONG).show();

                            showSuccess("Couldn't get link");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }
    }

    @SuppressLint("NewApi")
    void saveTask() {
        RequestModel rm = new RequestModel();

        String hourString = tp_timepicker.getHour() < 10 ? "0" + tp_timepicker.getHour() : "" + tp_timepicker.getHour();
        String minuteString = tp_timepicker.getMinute() < 10 ? "0" + tp_timepicker.getMinute() : "" + tp_timepicker.getMinute();
        String secondString = "00";
        String time = hourString + ":" + minuteString + ":" + secondString;

        Calendar calendar = Calendar.getInstance();
        calendar.set(dp_datepicker.getYear(), dp_datepicker.getMonth(), dp_datepicker.getDayOfMonth());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(calendar.getTime());
        String newdate = convertTimetoGMT(formatedDate + " " + time);

        rm.setTo_be_performed_at(newdate);
        ArrayList<String> leadlist = new ArrayList<>();
        leadlist.add(cm.get_id());
        rm.setLead_ids(leadlist);
        rm.setType(callType);

//        int checkedId = chiptasktype.getCheckedChipId();

//        switch (checkedId) {
//            case R.id.typecall:
//                rm.setType("call");
//                break;
//
//            case R.id.typefollow:
//                rm.setType("follow-up");
//                break;
//
//
//            case R.id.typeemail:
//                rm.setType("email");
//                break;
//
//            case R.id.typemeet:
//                rm.setType("meet");
//                break;
//
//
//        }
        ExtraModel extraModel = new ExtraModel();
        extraModel.setComplete(false);
        rm.setExtra_details(extraModel);

        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).CreateTask(leadlist, rm.getType(), rm.getTo_be_performed_at()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SimpleResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull SimpleResponseModel rm) {
                        hideProgress();


                        if (rm.isSuccess()) {
                            addtasklay.setVisibility(View.GONE);

                            showSuccess("Success");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                    callTask(cm);
                                }
                            }, 1500);


                            // Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                            // callTask(cm);

                        } else {
                            //  Toast.makeText(context,"Couldn't add task",Toast.LENGTH_LONG).show();

                            showSuccess("Couldn't add task");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
            ignored.printStackTrace();
        }

    }

    void saveInstantTask(String time) {
        RequestModel rm = new RequestModel();
        rm.setType("call");

        String newdate = convertTimetoGMT(time);
        rm.setTo_be_performed_at(newdate);
        ArrayList<String> leadlist = new ArrayList<>();
        leadlist.add(cm.get_id());
        rm.setLead_ids(leadlist);

        ExtraModel extraModel = new ExtraModel();
        extraModel.setComplete(false);
        rm.setExtra_details(extraModel);

        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).CreateTask(leadlist, rm.getType(), rm.getTo_be_performed_at()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SimpleResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull SimpleResponseModel rm) {
                        hideProgress();
                        if (rm.isSuccess()) {
                            addtasklay.setVisibility(View.GONE);

                            showSuccess("Success");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                            // Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                            //    callTask(cm);

                        } else {
                            //  Toast.makeText(context,"Couldn't add task",Toast.LENGTH_LONG).show();

                            showSuccess("Couldn't add task");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }

    }

    public void saveNote() {
        NoteModel rm = new NoteModel();
        addnotecalled = false;
        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).CreateNote("" + cm.get_id(), "" + noteDesc.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SimpleResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull SimpleResponseModel rm) {
                        hideProgress();
                        if (rm.isSuccess()) {
                            addnotelay.setVisibility(View.GONE);
                            noteDesc.setText("");
                            showSuccess("Success");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                    callNote(cm);
                                    hidePopup();
                                }
                            }, 1500);

                            //  Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                            // callNote(cm);

                        } else {
                            showSuccess("Couldn't add Note");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);
                            //   Toast.makeText(context,"Couldn't add Note",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }

    }


    public void callcontent(String val) {
        String s = val.toLowerCase();
        nulllay.setVisibility(View.GONE);
//        if (s.equals("all")) {
//            callcontent("message");
//            callcontent("file");
//            callcontent("page");
//            return;
//        }
        try {
            if (disposable != null) {
                showProgress();
                RequestModel note = new RequestModel();
                note.setType(s);
                showSuccess("Fetching content please wait..");
                if (s.equals("all")) callDisposableAll();
                else callDisposableSpacific(s);
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
            ignored.printStackTrace();
        }
    }


    private void callDisposableSpacific(String s) {
        disposable.add(RetroFit.get1(context).GetContentSpacific("" + cm.get_id(), "false", "1", "50", "" + s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<ContentResponseModel>() {
            @Override
            public void onSuccess(@NonNull ContentResponseModel rm) {
                hideProgress();
                hideSuccess();

                //    Log.d("val",String.valueOf(rm));
                if (rm.isStatus() && rm.getStatusCode() == 200 && rm.getData().size() > 0) {
                    contentlist.clear();
                    setContentData(rm.getData());
                    nulllay.setVisibility(View.GONE);
                } else {
                    nulllay.setVisibility(View.VISIBLE);
                    nulllay.setText("\n \n No Content Found        ");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                hideProgress();
                showSuccess(String.valueOf(e));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideSuccess();
                    }
                }, 1500);
                Log.d("Manoj", String.valueOf(e));
            }
        }));

    }

    private void callDisposableAll() {
        if (cm.get_id() != null) {
            Log.e("TAG", "callDisposableAll: id " + cm.get_id());
            disposable.add(RetroFit.get1(context).GetContent("" + cm.get_id(), "false", "1", "50").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<ContentResponseModel>() {
                @Override
                public void onSuccess(@NonNull ContentResponseModel rm) {
                    hideProgress();
                    hideSuccess();

                    //    Log.d("val",String.valueOf(rm));
                    if (rm.isStatus() && rm.getStatusCode() == 200 && rm.getData().size() > 0) {
                        contentlist.clear();
                        setContentData(rm.getData());
                        nulllay.setVisibility(View.GONE);
                        chipRemind.setVisibility(View.VISIBLE);
                        txStatus.setVisibility(View.VISIBLE);
                        lytEdit.setVisibility(View.VISIBLE);

                    } else {
                        nulllay.setVisibility(View.VISIBLE);
                        nulllay.setText("\n \n No Content Found        ");
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    hideProgress();
                    showSuccess(String.valueOf(e));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideSuccess();
                        }
                    }, 1500);
                    Log.d("Manoj", String.valueOf(e));
                }
            }));

        }
//        disposable.add(RetroFit.get1(context).GetContent("" + cm.get_id(), "false", "1", "50").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<ContentResponseModel>() {
//            @Override
//            public void onSuccess(@NonNull ContentResponseModel rm) {
//                hideProgress();
//                hideSuccess();
//
//                //    Log.d("val",String.valueOf(rm));
//                if (rm.isStatus() && rm.getStatusCode() == 200 && rm.getData().size() > 0) {
//                    contentlist.clear();
//                    setContentData(rm.getData());
//                    nulllay.setVisibility(View.GONE);
//                    chipRemind.setVisibility(View.VISIBLE);
//                    txStatus.setVisibility(View.VISIBLE);
//                    lytEdit.setVisibility(View.VISIBLE);
//
//                } else {
//                    nulllay.setVisibility(View.VISIBLE);
//                    nulllay.setText("\n \n No Content Found        ");
//                }
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                hideProgress();
//                showSuccess(String.valueOf(e));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideSuccess();
//                    }
//                }, 1500);
//                Log.d("Manoj", String.valueOf(e));
//            }
//        }));

    }

    public void callActivity(ResponseModel.Data cmr) {

        if (cm == null) {
            basictab.setVisibility(View.GONE);
            txtLabelTimeline.setVisibility(View.GONE);
            return;
        }
        try {
            if (cm.get_id() != null && disposable != null) {
                showProgress();
                nulllay.setVisibility(View.GONE);
                showSuccess("Fetching activity please wait..");

                disposable.add(RetroFit.get1(context).GetActivity("performedAt", "false", "1", "50", "false", "" + cm.get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<ActivityResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull ActivityResponseModel rm) {
                        hideProgress();
                        hideSuccess();
                        if (rm.isSuccess() && rm.getStatus() == 200 && rm.getData().size() > 0) {
                            setActivity(rm.getData());
                            EmptyMessageForTime.setVisibility(View.GONE);
                            activity_views.setVisibility(View.VISIBLE);
                            chipRemind.setVisibility(View.VISIBLE);
                            txStatus.setVisibility(View.VISIBLE);
                            lytEdit.setVisibility(View.VISIBLE);

                        } else {
                            activitylist = new ArrayList<>();
                            activityAdapter = new ActivityAdapter(context, activitylist);
                            activity_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            activity_views.setAdapter(activityAdapter);
                            activity_views.setVisibility(View.INVISIBLE);
                            EmptyMessageForTime.setVisibility(View.VISIBLE);
                            EmptyMessageForTime.setText("No Activity Found");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }
    }

    boolean addnotecalled = false;

    public void callNote(ResponseModel.Data cmr) {
        if (cm == null) {
            notelay.setVisibility(View.GONE);
            return;
        }
        try {
            if (cm.get_id() != null && disposable != null) {
                showProgress();
                nulllay.setVisibility(View.GONE);
                showSuccess("Fetching notes please wait..");
                disposable.add(RetroFit.get1(context).GetNotes("" + cm.get_id(), "false", "1", "50", "" + cm.get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<NoteResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull NoteResponseModel rm) {
                        hideProgress();
                        hideSuccess();
                        if (rm.isSuccess() && rm.getStatus() == 200 && rm.getData().size() > 0) {
                            setNoteData(rm.getData());
                            chipRemind.setVisibility(View.VISIBLE);
                            txStatus.setVisibility(View.VISIBLE);
                            lytEdit.setVisibility(View.VISIBLE);


                        } else {
                            if (!addnotecalled) {
//                                nulllay.setVisibility(View.VISIBLE);
//                                nulllay.setText("No Notes Found");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }
    }

    public void callTask(ResponseModel.Data cmr) {
        if (cm == null) {
            tasklay.setVisibility(View.GONE);
            return;
        }
        try {
            if (cm.get_id() != null && disposable != null) {
                showProgress();
                nulllay.setVisibility(View.GONE);

                showSuccess("Fetching tasks please wait..");
                disposable.add(RetroFit.get1(context).GetTasks("tobeperfomedat", "false", "1", "50", "" + cm.get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<TaskResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull TaskResponseModel rm) {
                        hideProgress();

                        hideSuccess();
                        if (rm.isSuccess() && rm.getStatus() == 200 && rm.getData().size() > 0) {
                            EmptyMessageForTask.setVisibility(View.GONE);
                            task_views.setVisibility(View.VISIBLE);
                            setTaskData(rm.getData());
                            chipRemind.setVisibility(View.VISIBLE);
                            txStatus.setVisibility(View.VISIBLE);
                            lytEdit.setVisibility(View.VISIBLE);

                        } else {
                            tasklist = new ArrayList<>();
                            Collections.reverse(tasklist);
                            taskAdapter = new TaskAdapter(context, tasklist);
                            task_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            task_views.setAdapter(taskAdapter);
                            taskAdapter.notifyDataSetChanged();
                            task_views.setVisibility(View.GONE);
                            EmptyMessageForTask.setVisibility(View.VISIBLE);
                            EmptyMessageForTask.setText("No Tasks Found");

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }
    }

    void setNoteData(ArrayList<NoteModel> noteData) {
        if (noteData.isEmpty() && noteData == null) {
            lytAddNotes.setVisibility(View.VISIBLE);
            notelistview.setVisibility(View.VISIBLE);
            addnotelay.setVisibility(View.GONE);
            nulllay.setVisibility(View.VISIBLE);
            nulllay.setText("No Notes Found");
        } else {
            lytAddNotes.setVisibility(View.VISIBLE);
            notelistview.setVisibility(View.VISIBLE);
            addnotelay.setVisibility(View.GONE);
            notelist = noteData;
            noteAdapter = new NoteAdapter(context, notelist);
            note_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            note_views.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
        }
    }

    void setEditStatus(List<StatusModel> user_status) {
        editStatusAdapter = new EditStatusAdapter(context, user_status, new EditStatusAdapter.ClickListener() {
            @Override
            public void onClick(int s) {
                chipRemind.setVisibility(View.VISIBLE);
                txStatus.setVisibility(View.VISIBLE);
                lytEdit.setVisibility(View.VISIBLE);
                bottom_sheet_edit_status.setVisibility(View.GONE);
                consl.setVisibility(View.VISIBLE);

                callUpdateStatus(user_status.get(s).getName());

            }
        });
        rvEditStatusList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        rvEditStatusList.setAdapter(editStatusAdapter);
        editStatusAdapter.notifyDataSetChanged();
    }

    private void callUpdateStatus(String status) {
        ArrayList<String> leadlist = new ArrayList<>();
        leadlist.add(cm.get_id());

        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).updateStatus(leadlist, status).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SimpleResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull SimpleResponseModel rm) {
                        hideProgress();

                        if (rm.isSuccess()) {

                            showSuccess("Success");

//                            callApi();
                            hideFab();
                            hidePopup();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        } else {
                            showSuccess("Couldn't add task");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        hideFab();
                        hidePopup();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            hideFab();
            hidePopup();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }

    }

    void setEditLabel(List<LabelModel> user_label) {

        editlabelAdapter = new EditlabelAdapter(context, user_label, (s, b) -> {
            chipRemind.setVisibility(View.VISIBLE);
            txStatus.setVisibility(View.VISIBLE);
            lytEdit.setVisibility(View.VISIBLE);

            if (b) {
                labelList.add(user_label.get(s).getName());
            } else labelList.remove(user_label.get(s).getName());

        });

        rvEditLabelList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        rvEditLabelList.setAdapter(editlabelAdapter);
        editlabelAdapter.notifyDataSetChanged();
    }

    private void callUpdateLabel(ArrayList<String> labelList) {

        ArrayList<String> leadlist = new ArrayList<>();
        leadlist.add(cm.get_id());

        try {
            if (disposable != null) {
                showProgress();
                disposable.add(RetroFit.get1(context).updateLabel(leadlist, labelList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SimpleResponseModel>() {
                    @Override
                    public void onSuccess(@NonNull SimpleResponseModel rm) {
                        hideProgress();
                        if (rm.isSuccess()) {

                            showSuccess("Success");

//                            callApi();
                            hideFab();
                            hidePopup();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        } else {
                            showSuccess("Couldn't add task");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSuccess();
                                }
                            }, 1500);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideProgress();
                        showSuccess(String.valueOf(e));
                        Handler handler = new Handler();
                        hideFab();
                        hidePopup();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSuccess();
                            }
                        }, 1500);
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {
            showSuccess(String.valueOf(ignored));
            Handler handler = new Handler();
            hideFab();
            hidePopup();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSuccess();
                }
            }, 1500);
        }
    }

    void setActivity(ArrayList<ActivityModel> noteData) {
        activitylist = noteData;
        activityAdapter = new ActivityAdapter(context, activitylist);
        activity_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        activity_views.setAdapter(activityAdapter);

    }

    void setContentData(ArrayList<ContentModel> contentData) {

        contentlist.addAll(contentData);
        //   Log.d("size",contentlist.size()+" sz");
        contentAdapter.notifyDataSetChanged();


    }


    void setTaskData(ArrayList<TaskModel> taskData) {
        Collections.reverse(taskData);
        tasklist = taskData;

        taskAdapter = new TaskAdapter(context, tasklist);
        task_views.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        task_views.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
    }

    private void showFabPopup() {
        try {
            if (collapsedView != null && collapsedView.getVisibility() == View.VISIBLE) {
                if (NetworkUtil.isNetworkConnected(context) && cm != null) {
                    showPopup();
                }
                //  callActivity(cm);
            } else if (expandedView != null && expandedView.getVisibility() == View.VISIBLE) {
                showFab();
            }
        } catch (Exception ignored) {
        }
    }

    private void showFab() {

        gosmall();
        try {

            rootframe.setFocusableInTouchMode(false);
            rootframe.clearFocus();


            rootframe.setOnKeyListener(null);
          /*  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                floatparams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }else{
                floatparams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            mWindowManager.updateViewLayout(mFloatingWidgetView, floatparams);*/
            Cursor cursor = null;
            cursor = asSqlLite.getAllIgnoreList();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String phone = cursor.getString(3);
                        if (phone.equals(mobileNumber)) {
                            hideFab();
                            return;
                        }

                    } while (cursor.moveToNext());

                }
                cursor.close();
            }

            hidePopup();
            if (layCons != null)
                layCons.setBackgroundColor(ContextCompat.getColor(context, R.color.bgTrans));
            if (collapsedView != null && collapsedView.getVisibility() != View.VISIBLE)
                collapsedView.setVisibility(View.VISIBLE);

            if (cm == null) {
                chipPopUp.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void hideFab() {
//        View view =
//        Log.e("TAG", "hideFab: :");
        if (collapsedView != null) collapsedView.setVisibility(View.GONE);
    }

    public void gobig() {
        Log.d("go big", "big called");
        try {
            floatparams.width = WindowManager.LayoutParams.MATCH_PARENT;
            floatparams.flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;


            mWindowManager.updateViewLayout(mFloatingWidgetView, floatparams);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gosmall() {

        try {
            floatparams.width = WindowManager.LayoutParams.MATCH_PARENT;
            floatparams.flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            mWindowManager.updateViewLayout(mFloatingWidgetView, floatparams);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopup() {
        Cursor cursor = null;
        cursor = asSqlLite.getAllIgnoreList();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String phone = cursor.getString(3);
                    if (phone.equals(mobileNumber)) {
                        hideFab();
                        return;
                    }

                } while (cursor.moveToNext());

            }
            cursor.close();
        }


        try {
            rootframe.setFocusableInTouchMode(true);
            rootframe.requestFocus();


            rootframe.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                        if (isViewCollapsed()) {
                            hideFab();
                            hidePopup();
                        } else {
                            hideFab();
                            hidePopup();
                            Log.d("return true", "true");
                            return true;

                        }

                    }

                    return false;
                }
            });

            handler = new Handler();
            dismissDialogRunnable = new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "Hide PopUp");
//                    hidePopup();
                }
            };

            expandedView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("TAG", "run: Touch On View");
                    resetDismissHandler();
                    return false;
                }
            });
            resetDismissHandler();
       /*     if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                floatparams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }else{
                floatparams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            mWindowManager.updateViewLayout(mFloatingWidgetView, floatparams);
*/
            gobig();
            hideFab();
            if (cm != null) {
                if (expandedView != null && expandedView.getVisibility() != View.VISIBLE)
                    expandedView.setVisibility(View.VISIBLE);

                if (laychange != null && laychange.getVisibility() != View.VISIBLE) {
                    laychange.setVisibility(View.VISIBLE);
                }
                if (llCrateFlowUpTask != null && llCrateFlowUpTask.getVisibility() != View.VISIBLE) {
                    llCrateFlowUpTask.setVisibility(View.VISIBLE);
                }
                if (addnotelay != null && addnotelay.getVisibility() != View.VISIBLE) {
                    addnotelay.setVisibility(View.VISIBLE);
                }
                if (notelistview != null && notelistview.getVisibility() != View.GONE) {
                    notelistview.setVisibility(View.GONE);
                }
                if (lytAddNotes != null && lytAddNotes.getVisibility() != View.GONE) {
                    lytAddNotes.setVisibility(View.GONE);
                }
                if (basictab != null && basictab.getVisibility() != View.GONE) {
                    basictab.setVisibility(View.GONE);
                }
                if (tasklay != null && tasklay.getVisibility() != View.GONE) {
                    tasklay.setVisibility(View.GONE);
                }
                if (contentlay != null && contentlay.getVisibility() != View.GONE) {
                    contentlay.setVisibility(View.GONE);
                }
                if (notelay != null && notelay.getVisibility() != View.VISIBLE) {
                    notelay.setVisibility(View.VISIBLE);
                }


                if (layCons != null)
                    layCons.setBackgroundColor(ContextCompat.getColor(context, R.color.bgTransHalf));

                getFileToUpload();
            } else {
                if (expandedView != null && expandedView.getVisibility() != View.VISIBLE)
                    expandedView.setVisibility(View.VISIBLE);

                if (laychange.getVisibility() != View.VISIBLE) {
                    laychange.setVisibility(View.VISIBLE);
                }
                if (notelay.getVisibility() != View.GONE) {
                    notelay.setVisibility(View.GONE);
                }
                if (llCrateFlowUpTask.getVisibility() != View.GONE) {
                    llCrateFlowUpTask.setVisibility(View.GONE);
                }

                if (layCons != null)
                    layCons.setBackgroundColor(ContextCompat.getColor(context, R.color.bgTransHalf));

            }

        } catch (Exception ignored) {

        }
    }

    private void hidePopup() {
        if (handler != null && dismissDialogRunnable != null) {

            handler.removeCallbacks(dismissDialogRunnable);
        }
        if (expandedView != null) expandedView.setVisibility(View.GONE);

    }


    private void showProgress() {
        try {
            if (progress != null && expandedView.getVisibility() == View.VISIBLE)
                progress.setVisibility(View.VISIBLE);


        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    private void hideProgress() {
        try {
            if (progress != null) progress.setVisibility(View.GONE);


        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void showSuccess(String text) {
        try {
//            success.setText(text);
            progress.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void hideSuccess() {
        try {
            progress.setVisibility(View.GONE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void txActive(int position) {
        try {
            resetDismissHandler();
            int i = 0;
            for (AppCompatTextView textView : txView) {
                if (i == position) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, icActiveIds[i], 0, 0);
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.icDark));
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, icIds[i], 0, 0);
                }
                i++;
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            resetDismissHandler();
            nulllay.setVisibility(View.GONE);
            Log.d("here", "inside switch api");
            addnotecalled = false;
            rootframe.setFocusableInTouchMode(true);
            rootframe.requestFocus();
            switch (position) {
                case 0:
                    basictab.setVisibility(View.VISIBLE);
                    txtLabelTimeline.setVisibility(View.VISIBLE);
                    tasklay.setVisibility(View.GONE);
                    notelay.setVisibility(View.GONE);
                    contentlay.setVisibility(View.GONE);
                    chipRemind.setVisibility(View.GONE);
                    txStatus.setVisibility(View.GONE);
                    lytAddNotes.setVisibility(View.GONE);
                    notelistview.setVisibility(View.GONE);
                    callActivity(cm);
                    break;

                case 1:
                    basictab.setVisibility(View.GONE);
                    txtLabelTimeline.setVisibility(View.GONE);
                    tasklay.setVisibility(View.VISIBLE);
                    notelay.setVisibility(View.GONE);
                    chipRemind.setVisibility(View.GONE);
                    txStatus.setVisibility(View.GONE);
                    contentlay.setVisibility(View.GONE);
                    lytAddNotes.setVisibility(View.GONE);
                    notelistview.setVisibility(View.GONE);
                    callTask(cm);
                    break;

                case 2:
                    basictab.setVisibility(View.GONE);
                    txtLabelTimeline.setVisibility(View.GONE);
                    tasklay.setVisibility(View.GONE);
                    notelay.setVisibility(View.VISIBLE);
                    chipRemind.setVisibility(View.GONE);
                    txStatus.setVisibility(View.GONE);
                    contentlay.setVisibility(View.GONE);

                    callNote(cm);
                    break;

                case 3:
                    basictab.setVisibility(View.GONE);
                    txtLabelTimeline.setVisibility(View.GONE);
                    tasklay.setVisibility(View.GONE);
                    notelay.setVisibility(View.GONE);
                    chipRemind.setVisibility(View.GONE);
                    txStatus.setVisibility(View.GONE);
                    contentlay.setVisibility(View.VISIBLE);
                    lytAddNotes.setVisibility(View.GONE);
                    notelistview.setVisibility(View.GONE);
                    callcontent("All");
                    break;
            }
        }
    }

    private String getName(String number) {
        String name = unknown;
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
                String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int indexName = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                if (cursor != null) {
                    if (cursor.moveToFirst() && cursor.getString(indexName) != null)
                        name = Utils.checkStr(cursor.getString(indexName));
                    else if (cursor.moveToLast() && cursor.getString(indexName) != null)
                        name = Utils.checkStr(cursor.getString(indexName));
                    cursor.close();
                }
            }
        } catch (Exception ignored) {
        }
        return name;
    }

    public void setData(CallModel callModel) {
        try {
            if (callModel != null) {
//                showFab();
//                setDataForPopUp(callModel);
//                mobileNumber = null;
                if (txCallType != null) {
                    switch (callModel.getCallType()) {
                        case "Incoming Call":
                            txCallType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_incoming_small, 0, 0, 0);
                            txCallType.setText(Utils.checkStr(callModel.getCallType()));
                            break;
                        case "Outgoing Call":
                            txCallType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outgoing_small, 0, 0, 0);
                            txCallType.setText(Utils.checkStr(callModel.getCallType()));
                            break;
                        case "Missed Call":
                            txCallType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_missed, 0, 0, 0);
                            txCallType.setText(Utils.checkStr(callModel.getCallType()));
                            break;
                    }
                    mCallType = callModel.getCallType();
//                    if (callModel.getDuration() != null && !callModel.getDuration().equals("") && !callModel.getDuration().equals("0") && !callModel.getCallType().toLowerCase().contains("missed")) {
//                        String s = DateUtils.formatElapsedTime(Long.parseLong(Utils.checkStr(callModel.getDuration())));
//                        Log.d("time", "hey" + callModel.getDuration() + "  " + s);
//                        txCallType.setText("Call Duration: " + s);
//                    } else {
//                        txCallType.setText(Utils.checkStr(callModel.getCallType()));
//                    }
                }
                if (txCallTopType != null) {

                    switch (callModel.getCallType()) {
                        case "Incoming Call":
                            icCallTypePopUp.setImageResource(R.drawable.ic_incoming_small);
                            txCallTopType.setText(Utils.checkStr(callModel.getCallType()));
                            icCallTypePopUp.setColorFilter(ContextCompat.getColor(context, R.color.my_tint_color), PorterDuff.Mode.SRC_IN);
                            break;
                        case "Outgoing Call":
                            icCallTypePopUp.setImageResource(R.drawable.ic_outgoing_small);
                            txCallTopType.setText(Utils.checkStr(callModel.getCallType()));
                            icCallTypePopUp.setColorFilter(ContextCompat.getColor(context, R.color.my_tint_color), PorterDuff.Mode.SRC_IN);

                            break;
                        case "Missed Call":
                            icCallTypePopUp.setImageResource(R.drawable.ic_missed);
                            txCallTopType.setText(Utils.checkStr(callModel.getCallType()));
                            break;
                    }
                    mCallType = callModel.getCallType();
//                    if (callModel.getDuration() != null && !callModel.getDuration().equals("") && !callModel.getDuration().equals("0") && !callModel.getCallType().toLowerCase().contains("missed")) {
//                        String s = DateUtils.formatElapsedTime(Long.parseLong(Utils.checkStr(callModel.getDuration())));
//                        Log.d("time", "hey" + callModel.getDuration() + "  " + s);
//                        txCallTopType.setText("Call Duration: " + s);
//                    } else {
//                        txCallTopType.setText(Utils.checkStr(callModel.getCallType()));
//                    }
                }
             /*   if (txCallerName != null) {
                    if (!Utils.checkStr(callModel.getName()).isEmpty())
                        txCallerName.setText(Utils.checkStr(callModel.getName()));
                    else txCallerName.setText(unknown);
                } */
                if (txCallerNo != null) {
                    mobileNumber = Utils.checkStr(callModel.getNumber());
                    txCallerNo.setText(mobileNumber);
                }

                callApi();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void setDataForPopUp(ResponseModel.Data callModel) {
        if (txCallUserName != null) {
            if (!Utils.checkStr(callModel.getName()).isEmpty())
                txCallUserName.setText(Utils.checkStr(callModel.getName()));
            else txCallUserName.setText(unknown);
        }
        if (txCallUserNumber != null) {
            mobileNumber = Utils.checkStr(callModel.getPhone());
            txCallUserNumber.setText(mobileNumber);
        }
        if (textPopup != null) {
            if (callModel.getIntegration().isEmpty() && callModel.getIntegration() == null) {
                textPopup.setText(Utils.getLeadName(callModel.getCustomSource(), convertTime(cm.getCreatedAt())));
            } else {
                textPopup.setText(Utils.getLeadName(callModel.getIntegration(), convertTime(cm.getCreatedAt())));
            }
        }

        ArrayList<LabelModel> labelList = new ArrayList<>();
        List<String> strLabels = new ArrayList<>();
        List<String> strStatus = new ArrayList<>();

        if (!callModel.getStatus().isEmpty()) {
            strStatus.addAll(callModel.getStatus());
        }

        if (callModel.getSaleValue() != null && !callModel.getSaleValue().isEmpty()) {
            strLabels.add(callModel.getSaleValue());
        }

        if (!callModel.getLabel().isEmpty()) {
            strLabels.addAll(callModel.getLabel());
        }
        Log.i("OverlayWindow", "setDataForPopUp: strLabels " + strLabels);
        Log.i("OverlayWindow", "setDataForPopUp: getUser_status " + strStatus);


        if (!cm.getStatus().isEmpty()) {
            txtEditStatus.setText(cm.getStatus().get(0));
        }
        if (!cm.getLabel().isEmpty()) {
            txtEditLabel.setText(cm.getLabel().get(0));
        }
        for (String s : strStatus) {
            boolean matchFound = false;  // To track if a match is found

            for (StatusModel s1 : callModel.getUserPreference().getUser_status()) {
                // Compare with StatusModel's name and value
                if (s.equalsIgnoreCase(s1.getValue()) || s.equalsIgnoreCase(s1.getName())) {
                    Log.i("OverlayWindow", "s: " + s + " s1.getValue(): " + s1.getValue());

                    // Create a new label model based on the matched status
                    LabelModel labelModel = new LabelModel("", "", "");
                    labelModel.setColor(s1.getColor());
                    txtEditStatus.setText(s1.getName());
                    labelModel.setName(s1.getName());
                    labelList.add(labelModel);

                    // Add the label to the pop-up
                    addLabelChipForPopUp(labelList);

                    // Mark that a match was found
                    matchFound = true;
                    break;  // Exit inner loop when a match is found
                }
            }

            // If no match is found, add a custom label
            if (!matchFound) {
                Log.i("OverlayWindow", "Custom label added for: " + s);

                LabelModel customLabelModel = new LabelModel("", "", "");
                customLabelModel.setColor("#FFFF1800");  // Custom color, modify as needed
                customLabelModel.setName(s.replace("_"," "));  // Customize the name
                txtEditStatus.setText(s.replace("_"," "));  // Optionally update the status display
                labelList.add(customLabelModel);

                // Add the custom label to the pop-up
                addLabelChipForPopUp(labelList);
            }
        }


        for (String s : strLabels) {
            boolean matchFound = false;  // Track if a match is found for each label

            for (LabelModel s1 : callModel.getUserPreference().getUser_Label()) {
                // Compare with LabelModel's name and value
                if (s.equalsIgnoreCase(s1.getValue()) || s.equalsIgnoreCase(s1.getName())) {
                    Log.i("OverlayWindow", "s: " + s + " s1.getValue(): " + s1.getValue());

                    // Create a new label model based on the matched label
                    LabelModel labelModel = new LabelModel("", "", "");
                    labelModel.setColor(s1.getColor());
                    labelModel.setName(s);  // Use the name from strLabels
                    labelList.add(labelModel);

                    // Add the label to the pop-up
                    addLabelChipForPopUp(labelList);

                    // Mark that a match was found
                    matchFound = true;
                    break;  // Exit the inner loop when a match is found
                }
            }

            // If no match is found, add a custom label
            if (!matchFound) {
                Log.i("OverlayWindow", "Custom label added for: " + s);

                // Create a custom label model with default or custom properties
                LabelModel customLabelModel = new LabelModel("", "", "");
                customLabelModel.setColor("#FF00FF5F");  // Custom color (e.g., red), modify as needed
                customLabelModel.setName(s.replace("_", " "));  // Custom label name
                labelList.add(customLabelModel);

                // Add the custom label to the pop-up
                addLabelChipForPopUp(labelList);
            }
        }

    }

    public void setData1(CallModel callModel) {
        //  Log.d("setdata1",new Gson().toJson(callModel));
        try {
            if (callModel != null) {
//                if (NetworkUtil.isNetworkConnected(context) && cm != null) {
//                    showFab();
//                }
                mobileNumber = null;
//                if (txCallType != null) {
//                    switch (callModel.getCallType()) {
//
//                        case "Incoming Call":
//                            txCallType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_incoming_small, 0, 0, 0);
//                            break;
//                        case "Outgoing Call":
//                            txCallType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outgoing_small, 0, 0, 0);
//                            break;
//                        case "Missed Call":
//                            txCallType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_missed, 0, 0, 0);
//                            break;
//                    }
//                    mCallType = callModel.getCallType();
//                    if (callModel.getDuration() != null && !callModel.getDuration().equals("") && !callModel.getDuration().equals("0") && !callModel.getCallType().toLowerCase().contains("missed")) {
//                        //  String s = DateUtils.formatElapsedTime(Long.parseLong(Utils.checkStr(callModel.getDuration())));
//                        String s = DateUtils.formatElapsedTime(Long.parseLong(Utils.checkStr(callModel.getDuration())));
//                        Log.d("time", "hey" + callModel.getDuration() + "  " + s);
//
//                        txCallType.setText("Call Duration: " + Utils.checkStr(s));
//                    } else {
//                        txCallType.setText(Utils.checkStr(callModel.getCallType()));
//                    }
//                }
             /*   if (txCallerName != null) {
                    if (!Utils.checkStr(callModel.getName()).isEmpty())
                        txCallerName.setText(Utils.checkStr(callModel.getName()));
                    else txCallerName.setText(unknown);
                }*/
                if (txCallerNo != null) {
                    mobileNumber = Utils.checkStr(callModel.getNumber());
                    txCallerNo.setText(mobileNumber);
                }

//                callApi();
            }
            if (NetworkUtil.isNetworkConnected(context) && cm != null) {
                showPopup();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /*  Method to move the Floating widget view to Right  */
    private void moveToRight(final int current_x_cord) {
        try {
            boolean goleft = false;
            if (current_x_cord < szWindow.x / 2) {
                goleft = true;
            }
            Log.d("vale of move", current_x_cord + " " + szWindow.x / 2);
            boolean finalGoleft = goleft;
            new CountDownTimer(100, 100) {
                //get params of Floating Widget view
                WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

                public void onTick(long t) {
                    long step = (500 - t) / 5;

                    if (finalGoleft) {
                        mParams.x = (int) (current_x_cord - (current_x_cord * step) - mFloatingWidgetView.getWidth());
                    } else {
                        mParams.x = (int) (szWindow.x + current_x_cord + (current_x_cord * step) - mFloatingWidgetView.getWidth());
                    }
                    //  mParams.x = (int) (szWindow.x + (current_x_cord * current_x_cord * step) - mFloatingWidgetView.getWidth());

                    //If you want bounce effect uncomment below line and comment above line
                    //  mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - mFloatingWidgetView.getWidth();

                    //Update window manager for Floating Widget
                    //  mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);
                }

                public void onFinish() {
                    if (!finalGoleft) {

                        mParams.x = szWindow.x - mFloatingWidgetView.getWidth() / 2;
                    } else {
                        mParams.x = 0;
                    }
                    if (mFloatingWidgetView.getWindowToken() != null && mFloatingWidgetView.getParent() != null) {
                        //Update window manager for Floating Widget
                        mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);
                    }
                }
            }.start();
        } catch (Exception e) {

        }
    }

    /*  Get Bounce value if you want to make bounce effect to your Floating Widget */
    private double bounceValue(long step, long scale) {
        double value = scale * java.lang.Math.exp(-0.055 * step) * java.lang.Math.cos(0.08 * step);
        return value;
    }

    public void callApi() {
        //Log.d("here","call api"+mobileNumber);
        txCallerNo.setText(mobileNumber);
        txCallUserNumber.setText(mobileNumber);
        txCallerName.setText(unknown);
        txCallUserName.setText(unknown);

        String num = mobileNumber.replace("+91", "");
        try {
            // TODO: remove hard number
            if (mobileNumber != null && disposable != null) {
                showProgress();

                currentTime = Utils.getCurrentDate("yyMMdd_HHmm");
                disposable.add(RetroFit.get1(context).getUserByPhone(num)  //7015823248
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<ResponseModel>() {
                            @Override
                            public void onSuccess(@NonNull ResponseModel rm) {
                                hideProgress();
                                shimmerDymanicLayout.setVisibility(View.GONE);
                                Log.d("info", new Gson().toJson(rm.getData()) + "info");
                                //  if (rm.isSuccess() && rm.getStatus()==200) {
                                if (rm.getStatusCode() == 200) {
                                    setApiData(rm.getData());
                                    layFab.setVisibility(View.GONE);
                                } else {
                                    cm = null;
//                                    showFab();
//                                    chipStatus.setVisibility(View.GONE);
                                    chipRemind.setVisibility(View.GONE);
                                    txtLabelTimeline.setVisibility(View.GONE);
                                    txStatus.setVisibility(View.GONE);
                                    notelay.setVisibility(View.GONE);
                                    lytEdit.setVisibility(View.GONE);
//                                    showSuccess(Utils.checkStr(rm.mes()));
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideSuccess();
                                        }
                                    }, 1500);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                hideProgress();
                                cm = null;
                                shimmerDymanicLayout.setVisibility(View.GONE);
//                                chipStatus.setVisibility(View.GONE);
                                chipRemind.setVisibility(View.GONE);
                                txStatus.setVisibility(View.GONE);
                                lytEdit.setVisibility(View.GONE);
                                txtLabelTimeline.setVisibility(View.GONE);

//                                showFab();
                                DEEP_LINK_URL = "sigmacrm://create-lead/" + mobileNumber.replace("+91", "");
                                icSettings.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.add_white));
                              /*  showSuccess( String.valueOf(e));
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideSuccess();
                                    }
                                }, 1500); */
                                Log.d("Manoj", String.valueOf(e));
                            }
                        }));
            }
        } catch (Exception ignored) {

            ignored.printStackTrace();
        }

    }


    private void getFileToUpload() {

        try {

            String path = "/storage/emulated/0/Recordings/Call";

            File directory = new File(path);
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().toLowerCase().contains(currentTime)) {

                    fileName = files[i].getName();
                    break;
                }
            }
            Log.d("FilesHere", "FileName:" + path + "/" + fileName);
            if (fileName != "") {
                String mFoundFile = path + "/" + fileName;
//                uploadFile(mFoundFile,leadIds);
                fileName = "";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void uploadFile(String s, String leadIds) {

        MultipartBody.Part requestFile;


        RequestBody leadId = RequestBody.create(MediaType.parse("multipart/form-data"), "62c70e0a15bd971464628944");
        RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), "" + mCallType);
        RequestBody performedAt = RequestBody.create(MediaType.parse("multipart/form-data"), "2022-07-10T16:21:23.081Z"/*+Utils.getCurrentDate("dd-mm-yyyy hh:mm a")*/);

        File file1 = new File(s);
        RequestBody requestFile1 = RequestBody.create(file1, MediaType.parse("audio/m4a"));
        requestFile = MultipartBody.Part.createFormData("file", file1.getName(), requestFile1);


        try {
            if (disposable != null) {
                disposable.add(RetroFit.get1(context).addAttachment(leadId, type, performedAt, requestFile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<UploadImage>() {
                    @Override
                    public void onSuccess(@NonNull UploadImage rm) {
                        if (rm.isStatus()) {


                        } else {


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("Manoj", String.valueOf(e));
                    }
                }));
            }
        } catch (Exception ignored) {

        }

    }

    private String convertTime(String capString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        dateFormat.setTimeZone(timeZone);
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

        String dateStr2 = "";
        try {
            date = dateFormat.parse(capString);


            TimeZone t1 = TimeZone.getTimeZone("Asia/Kolkata");
            DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //If you need time just put specific format for time like 'HH:mm:ss'
            formatter2.setTimeZone(t1);
            dateStr2 = formatter2.format(date);

            //    Log.d("time",dateStr2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr2;

    }

    private String convertTimetoGMT(String capString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");

        dateFormat.setTimeZone(timeZone);
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

        String dateStr2 = "";
        try {
            date = dateFormat.parse(capString);

            TimeZone t1 = TimeZone.getTimeZone("GMT");

            DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //If you need time just put specific format for time like 'HH:mm:ss'
            formatter2.setTimeZone(t1);
            dateStr2 = formatter2.format(date);

            Log.d("time", dateStr2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr2;

    }

    private void setApiData(ResponseModel.Data cmr) {
        DEEP_LINK_URL = "sigmacrm://lead/" + cmr.get_id();
        icSettings.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info));

        cm = cmr;
        try {
            if (cm != null) {
                showFab();
                setDataForPopUp(cm);
                leadIds = cm.get_id();
                chipRemind.setVisibility(View.VISIBLE);
                txStatus.setVisibility(View.VISIBLE);
                lytEdit.setVisibility(View.VISIBLE);
                lytEdit.setVisibility(View.VISIBLE);
                tvNotes.setVisibility(View.VISIBLE);
//                tvNotes.setText(cm.des);
                if (txCallerName != null) {
                    if (!Utils.checkStr(cm.getName()).isEmpty()) {
                        txCallerName.setText(Utils.checkStr(cm.getName()));
                        txStatus.setText(" Remind to make another call with " + cm.getName() + " in");
                    } else txCallerName.setText(unknown);
                }
                if (txCallerNo != null) {
                    //  mobileNumber = Utils.checkStr(callModel.getNumber());
                    txCallerNo.setText(mobileNumber);
                }

                if (txIntegrationName != null) {
                    if (cm.getIntegration().isEmpty() && cm.getIntegration() == null) {
                        txIntegrationName.setText(Utils.getLeadName(cm.getCustomSource(), convertTime(cm.getCreatedAt())));
                    } else {
                        txIntegrationName.setText(Utils.getLeadName(cm.getIntegration(), convertTime(cm.getCreatedAt())));

                    }
                }
//                    txIntegrationName.setText(Utils.getLeadName(cm.getCustomSource(), convertTime(cm.getCreatedAt())));

//                List<LabelModel> lm = new ArrayList<>();
//                List<StatusModel> status = new ArrayList<>();
//                Boolean flag = false;
//                List<String> strLabels = new ArrayList(cm.getUserPreference().getUser_Label());
//                LabelModel statusModel = new LabelModel("", "#229ff2", "");
//                StatusModel statusModel11 = new StatusModel();
//
//                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
//                format.setMaximumFractionDigits(0);
//                format.setCurrency(Currency.getInstance("INR"));
//
//
//                if (cm.getUserPreference().getUser_status() != null && cm.getUserPreference().getUser_status().size() > 0) {
//
//                    setEditStatus(cm.getUserPreference().getUser_status());
//
//
//                    for (StatusModel s : cm.getUserPreference().getUser_status()) {
//                        if (s.getName().trim().equalsIgnoreCase(cm.getStatus().toString().replace("[", "").replace("]", ""))) {
//                            statusModel11.setColor(s.getColor());
//                            statusModel11.setValue(s.getValue());
//                            statusModel11.setName(s.getName());
//                            flag = true;
//                        }
//                    }
//
//
//                }
//
//                status.add(statusModel11);
//                lm.add(statusModel);
//
////                if (flag)
////                    chipStatus.setVisibility(View.VISIBLE);
////                else chipStatus.setVisibility(View.GONE);
//
////                addStatusChip(status);
//
//                if (cm.getUserPreference().getUser_Label() != null && cm.getUserPreference().getUser_Label().size() > 0) {
//                    setEditLabel(cm.getUserPreference().getUser_Label());
//                }
//
//                if (cm.getExtraDetails() != null && !Utils.checkStr(cm.getExtraDetails().getSale_value()).equals("")) {
//                    LabelModel statusModel1 = new LabelModel(format.format(Integer.parseInt(Utils.checkStr(cm.getExtraDetails().getSale_value()))), "#11abf4", format.format(Integer.parseInt(Utils.checkStr(cm.getExtraDetails().getSale_value()))));
//                    lm.add(statusModel1);
//
//                }
//
//                ArrayList<LabelModel> lm2 = new ArrayList<>();
//                Boolean flag2 = false;
//
//                for (StatusModel s : status) {
//                    LabelModel labelModel = new LabelModel("", "#229ff2", "");
//                    labelModel.setColor(s.getColor());
//                    labelModel.setValue(s.getValue());
//                    labelModel.setName(s.getName());
//
//                    lm2.add(labelModel);
//                }
//
//                if (cm.getUserPreference().getUser_Label() != null && cm.getUserPreference().getUser_Label().size() > 0) {
//
////                    setEditLabel(cm.getUserPreference().getUser_Label());
//
//                    for (LabelModel s : cm.getUserPreference().getUser_Label()) {
//                        LabelModel labelModel = new LabelModel("", "#229ff2", "");
//                        if (cm.getLabel().toString().toLowerCase().contains(s.getName().trim().toLowerCase())) {
//                            labelModel.setColor(s.getColor());
//                            labelModel.setValue(s.getValue());
//                            labelModel.setName(s.getName());
//                            flag2 = true;
//                        }
//                        if (labelModel.getName() != "") lm2.add(labelModel);
//
//                    }
//
//                }
//
//
//                if (flag2) chipInfo.setVisibility(View.VISIBLE);
//                else chipInfo.setVisibility(View.GONE);


//                addLabelChip(lm2);


            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }


    }

    private void resetDismissHandler() {

        Log.e("TAG", "resetDismissHandler");

        if (handler != null && dismissDialogRunnable != null) {
            handler.removeCallbacks(dismissDialogRunnable);
            handler.postDelayed(dismissDialogRunnable, 10000);
        }// 10 seconds
    }

    private void addLabelChipForPopUp(ArrayList<LabelModel> lm) {
        Log.i("OverlayWindow", "addLabelChipForPopUp :" + lm.size());
        try {
            if (chipPopUp != null) {
                if (chipPopUp.getChildCount() > 0) chipPopUp.removeAllViews();

                for (LabelModel l : lm) {
                    Chip chip = new Chip(new ContextThemeWrapper(context, R.style.CalledIdTheme));
                    chip.setText(Utils.checkStr(l.getName()));
                    chip.setTextColor(ContextCompat.getColor(context, R.color.white));
                    chip.setCheckable(false);
                    chip.setTextSize(12);
                    chip.setTextAlignment(Chip.TEXT_ALIGNMENT_CENTER);
                    chip.setRippleColor(null);
                    chip.setEnsureMinTouchTargetSize(false);
                    chip.setChipBackgroundColor(getColor(l.getColor()));
                    chip.setTypeface(ResourcesCompat.getFont(context, R.font.gilroy_bold));
                    chipPopUp.addView(chip);
                }
            }

        } catch (Exception e) {
            Log.d("Manoj", String.valueOf(e));
        }
    }

    private void addTaskChip(List<LabelModel> lm) {
        try {
            if (chiptasktype != null) {
                if (chiptasktype.getChildCount() > 0) chiptasktype.removeAllViews();


                callType = lm.get(0).getName();

                for (LabelModel l : lm) {
                    Chip chip = new Chip(new ContextThemeWrapper(context, R.style.CalledIdTheme));
                    chip.setText(Utils.checkStr(l.getName()));
                    chip.setTextColor(ContextCompat.getColor(context, R.color.white));
                    chip.setCheckable(true);
                    chip.setTextSize(16);
                    chip.setTextAlignment(Chip.TEXT_ALIGNMENT_CENTER);
                    chip.setRippleColor(null);
                    chip.setEnsureMinTouchTargetSize(false);
                    chip.setChipBackgroundColorResource(R.drawable.chip_selector);
                    chip.setTextColor(R.drawable.tx_selector);
                    chip.setTypeface(ResourcesCompat.getFont(context, R.font.gilroy_bold));
                    chiptasktype.addView(chip);


                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetDismissHandler();
                            callType = "" + chip.getChipText();
                        }
                    });


                }
            }

        } catch (Exception e) {
            Log.d("Manoj", String.valueOf(e));
        }
    }

    private void addLabelChip(List<LabelModel> lm) {
        try {
            if (chipInfo != null) {
                if (chipInfo.getChildCount() > 0) chipInfo.removeAllViews();

                for (LabelModel l : lm) {
                    Chip chip = new Chip(new ContextThemeWrapper(context, R.style.CalledIdTheme));
                    chip.setText(Utils.checkStr(l.getName()));
                    chip.setTextColor(ContextCompat.getColor(context, R.color.white));
                    chip.setCheckable(false);
                    chip.setTextSize(12);
                    chip.setTextAlignment(Chip.TEXT_ALIGNMENT_CENTER);
                    chip.setRippleColor(null);
                    chip.setEnsureMinTouchTargetSize(false);
                    chip.setChipBackgroundColor(getColor(l.getColor()));
                    chip.setTypeface(ResourcesCompat.getFont(context, R.font.gilroy_bold));
                    chipInfo.addView(chip);
                    chipInfo.addView(chip);


                }
            }

        } catch (Exception e) {
            Log.d("Manoj", String.valueOf(e));
        }
    }

//    private void addStatusChip(List<StatusModel> sm) {
//        try {
//            if (chipStatus != null) {
//                if (chipStatus.getChildCount() > 0) chipStatus.removeAllViews();
//                for (StatusModel s : sm) {
//                    Chip chip = new Chip(new ContextThemeWrapper(context, R.style.CalledIdTheme));
//                    chip.setText(Utils.checkStr(s.getName()));
//                    chip.setTextColor(ContextCompat.getColor(context, R.color.white));
//                    chip.setCheckable(false);
//                    chip.setTextAlignment(Chip.TEXT_ALIGNMENT_CENTER);
//                    chip.setRippleColor(null);
//                    chip.setEnsureMinTouchTargetSize(false);
//                    chip.setChipBackgroundColor(getColor(s.getColor()));
//                    chip.setTypeface(ResourcesCompat.getFont(context, R.font.gilroy_bold));
//                    chipStatus.addView(chip);
//                }
//            }
//        } catch (Exception e) {
//            Log.d("Manoj", String.valueOf(e));
//        }
//    }

    private ColorStateList getColor(String colorCode) {
        return new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor(colorCode)});
    }

    private void call() {
        try {
//            showFab();
            if (mobileNumber != null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    String mobile = Utils.checkStr(mobileNumber);
                    if (!Utils.isEmpty(mobile))
                        context.startActivity(new Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse("tel:" + mobile)));
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void whatsapp() {
        try {
            //  showFab();


            if (mobileNumber != null) {
                if (checkPackageExist("com.whatsapp") || checkPackageExist("com.whatsapp.w4b")) {
                    //  String  toNumber ="91"+mobileNumber.replace("+91", "").replace(" ", "");
                    String s1 = mobileNumber.replace("+91", "").replace(" ", "");
                    String toNumber = "+91" + StringUtils.right(s1, 10);

                    Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + toNumber));

                    context.startActivity(Intent.createChooser(whatsappIntent, "Check out Caller Id").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                } else {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        } catch (Exception ignored) {
        }
        close();
    }

    private void whatsapp(String text) {
        try {
            showFab();
            if (cm != null) {
                text = text.replace("@lead name", cm.getName());
            }
            if (mobileNumber != null) {
                if (checkPackageExist("com.whatsapp") || checkPackageExist("com.whatsapp.w4b")) {
                    String msg = URLEncoder.encode(text, "utf-8");
                    ;
                    //  Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?text=" + msg + "&phone=" + Utils.checkCountryCode(mobileNumber)));
                    //  Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/"+Utils.checkCountryCode(mobileNumber)+"?text=" + msg ));

                    //  String msg = text;
                    // String toNumber ="91"+ StringUtils.right(mobileNumber.replace("+91", "").replace(" ", ""),10);
                    String s1 = mobileNumber.replace("+91", "").replace(" ", "");
                    String toNumber = "+91" + StringUtils.right(s1, 10);

                    Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + toNumber + "?text=" + msg));

                    context.startActivity(Intent.createChooser(whatsappIntent, "Check out Caller Id").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                } else {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private boolean checkPackageExist(String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void close() {
        Log.v("close", "close called");
        try {
            OverlayService.overlayWindow = null;
            if (disposable != null) disposable.dispose();
            mWindowManager.removeView(mFloatingWidgetView);
            mFloatingWidgetView.invalidate();
            ViewGroup viewGroup = ((ViewGroup) mFloatingWidgetView.getParent());
            if (viewGroup != null && viewGroup.getChildCount() > 0) viewGroup.removeAllViews();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}