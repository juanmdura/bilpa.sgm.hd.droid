package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BaseActivity;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.CheckCommentsAdapter;
import com.bilpa.android.adapters.PendientesAdapter;
import com.bilpa.android.model.Comentario;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.comments.CommentsResult;
import com.bilpa.android.services.actions.comments.SaveCheckCommentAction;
import com.bilpa.android.services.actions.comments.UpdateCheckCommentAction;
import com.bilpa.android.services.actions.pendientes.PendientesResult;
import com.bilpa.android.utils.SessionStore;
import com.bilpa.android.widgets.OnAddEditPendientes;
import com.bilpa.android.widgets.ShowHideAddButton;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

public class CommentsFragment extends Fragment implements
        AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener  {

    public static final String TAG_COMMENT_DIALOG   = "TAG_COMMENT_DIALOG";
    public static final String M_DATA_RESULT        = "commentsResult";

    private ListView vList;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private CheckCommentsAdapter mCommentsAdapter;
    public Long chequeoId;
    public String chequeoNombre;
    public CommentsResult mCommentsResult;
    private Toolbar vSecondayToolbar;

    public static CommentsFragment newInstance(Long chequeoId, String chequeoNombre) {
        CommentsFragment fragment = new CommentsFragment();
        fragment.chequeoId = chequeoId;
        fragment.chequeoNombre = chequeoNombre;
        return fragment;
    }

    public CommentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);

        vSecondayToolbar = (Toolbar) v.findViewById(R.id.vSecondayToolbar);
        vList = (ListView) v.findViewById(R.id.vList);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        vList.setEmptyView(vEmptyView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Toolbar secundario
        vSecondayToolbar.getMenu().clear();
        vSecondayToolbar.setTitle(R.string.comments_list_title);
        vSecondayToolbar.setSubtitle(chequeoNombre);

        if (savedInstanceState != null && savedInstanceState.containsKey(M_DATA_RESULT)) {
            mCommentsResult = (CommentsResult) savedInstanceState.get(M_DATA_RESULT);
        }

        ((ShowHideAddButton) getActivity()).showAddBtn();

        ViewUtils.gone(vEmptyView, vProgress, vList);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        load();
    }

    private void load() {
        load(true);
    }

    private void load(boolean hideList) {

        if (hideList) {
            ViewUtils.gone(vList);
        }

        ViewUtils.gone(vEmptyView, vProgress);

        if (mCommentsResult == null) {
            ViewUtils.visible(vProgress);

            ApiService.getCheckComments(chequeoId, new AsyncCallback<CommentsResult>(getActivity()) {
                @Override
                protected void onSuccess(CommentsResult result) {
                    mCommentsResult = result;
                    bind(result);
                }
                @Override
                protected void onServiceOperationFailOK() {
                    super.onServiceOperationFailOK();
                    getActivity().onBackPressed();
                }
            });
        } else {
            bind(mCommentsResult);
        }
    }

    private void bind(CommentsResult result) {
        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vProgress);
        mCommentsAdapter = new CheckCommentsAdapter(getActivity(), result.comentarios);
        mCommentsAdapter.setOnDeleteListener(this);
        mCommentsAdapter.setOnCheckPrintableListener(this);
        vList.setAdapter(mCommentsAdapter);
        vList.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(M_DATA_RESULT, mCommentsResult);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_CHEQUEO_COMMENTS);
        app.cancelPendingRequests(ApiService.DELETE_CHECK_COMMENT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckCommentsAdapter adapter = (CheckCommentsAdapter) parent.getAdapter();
        Comentario comentario = adapter.getItem(position);
        showEditComment(comentario);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vDelete:
                final Comentario comment = (Comentario) v.getTag();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.app_name)
                        .iconRes(R.drawable.ic_launcher)
                        .content(getString(R.string.comments_msg_delete_comment))
                        .positiveText(R.string.btn_aceptar)
                        .negativeText(R.string.btn_cancelar)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                doDeleteComment(comment);
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.vCheckImprimible) {
            final Comentario comment = (Comentario) buttonView.getTag();
            comment.visible = isChecked;
            doUpdate(comment);
        }
    }

    private void doDeleteComment(final Comentario comentario) {
        ApiService.deleteComentario(Long.valueOf(comentario.id), new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                invalidateComments();
                load();
            }
        });
    }

    public void invalidateComments() {
        mCommentsResult = null;
    }

    public void doComment(String comment) {

        if (comment == null) {
            ToastUtils.showToast(getActivity(), R.string.comments_msg_empty_comment);
            return;
        }

        SaveCheckCommentAction.Builder builder = new SaveCheckCommentAction.Builder();
        builder.idItemChequeado = chequeoId;
        builder.texto = comment;
        builder.idUsuario = Long.valueOf(SessionStore.getSession(getActivity()).id);
        builder.imprimible = false;
        ApiService.saveCheckComment(builder, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                ToastUtils.showToast(getActivity(), R.string.comments_msg_success);
                invalidateComments();
                load();
            }
        });
    }

    public void doUpdate(Comentario comentario) {
        if (comentario == null) {
            ToastUtils.showToast(getActivity(), R.string.comments_msg_empty_comment);
            return;
        }

        UpdateCheckCommentAction.Builder builder = new UpdateCheckCommentAction.Builder();
        builder.id = Long.valueOf(comentario.id);
        builder.idItemChequeado = chequeoId;
        builder.texto = comentario.texto;
        builder.idUsuario = Long.valueOf(SessionStore.getSession(getActivity()).id);
        builder.imprimible = comentario.visible;
        ApiService.updateCheckComment(builder, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                ToastUtils.showToast(getActivity(), R.string.comments_msg_updated);
                invalidateComments();
                load(false);
            }
        });
    }

    public void showAddComment() {
        CommentDialogFragment commentDialogFragment = CommentDialogFragment.newInstance();
        commentDialogFragment.setOnCommentListener(new CommentDialogFragment.OnCommentListener() {
            @Override
            public void onComment(final String comment) {
                Fragment current = ((BaseActivity) getActivity()).peekFragment();
                if (current != null && CommentsFragment.class.isInstance(current)) {
                    CommentsFragment cf = (CommentsFragment) current;
                    cf.doComment(comment);
                }
            }
        });
        commentDialogFragment.show(getActivity().getSupportFragmentManager(), TAG_COMMENT_DIALOG);
    }

    public void showEditComment(final Comentario comment) {
        CommentDialogFragment commentDialogFragment = CommentDialogFragment.newInstance(comment.texto);
        commentDialogFragment.setOnCommentListener(new CommentDialogFragment.OnCommentListener() {
            @Override
            public void onComment(final String newComment) {
                Fragment current = ((BaseActivity) getActivity()).peekFragment();
                if (current != null && CommentsFragment.class.isInstance(current)) {
                    CommentsFragment cf = (CommentsFragment) current;
                    comment.texto = newComment;
                    cf.doUpdate(comment);
                }
            }
        });
        commentDialogFragment.show(getActivity().getSupportFragmentManager(), TAG_COMMENT_DIALOG);
    }

}
