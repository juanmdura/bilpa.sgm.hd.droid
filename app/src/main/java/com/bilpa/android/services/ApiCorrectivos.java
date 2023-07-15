package com.bilpa.android.services;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.pendientes.PendientesResult;
import com.bilpa.android.services.correctivos.CorregirActivoDeFallaReportadaAction;
import com.bilpa.android.services.correctivos.DeletePendienteCorregidoAction;
import com.bilpa.android.services.correctivos.DeleteSolucionAction;
import com.bilpa.android.services.correctivos.FinalizarCorrectivoAction;
import com.bilpa.android.services.correctivos.GetActivosConFallasAction;
import com.bilpa.android.services.correctivos.GetActivosConFallasResult;
import com.bilpa.android.services.correctivos.GetActivosNoReportadosAction;
import com.bilpa.android.services.correctivos.GetActivosNoReportadosResult;
import com.bilpa.android.services.correctivos.GetCorrectivosAction;
import com.bilpa.android.services.correctivos.GetCorrectivosResult;
import com.bilpa.android.services.correctivos.GetPendientesCorregidoAction;
import com.bilpa.android.services.correctivos.GetPreviewCorrectivoAction;
import com.bilpa.android.services.correctivos.GetPreviewCorrectivoResult;
import com.bilpa.android.services.correctivos.GetSolucionesAction;
import com.bilpa.android.services.correctivos.GetSolucionesResult;
import com.bilpa.android.services.correctivos.IniciarCorrectivoAction;
import com.bilpa.android.services.correctivos.ModificarCorrectivoAction;
import com.bilpa.android.services.correctivos.SavePendienteCorrectivoAction;
import com.bilpa.android.services.correctivos.SaveSolucionAction;

public class ApiCorrectivos {

    public static final String GET_CORRECTIVOS              = "GET_CORRECTIVOS";
    public static final String GET_ACTIVOS_FALLAS           = "GET_ACTIVOS_FALLAS";
    public static final String GET_ACTIVOS_NO_REPORTADOS    = "GET_ACTIVOS_NO_REPORTADOS" ;
    public static final String GET_SOLUCIONES               = "GET_SOLUCIONES";
    public static final String GET_PENDIENTES_CORREGIDO     = "GET_PENDIENTES_CORREGIDO";
    public static final String SAVE_PENDIENTE               = "SAVE_PENDIENTE";
    public static final String SAVE_SOLUCION                = "SAVE_SOLUCION";
    public static final String DELETE_SOLUCION              = "DELETE_SOLUCION";
    public static final String DELETE_PENDIENTE             = "DELETE_PENDIENTE";
    public static final String INICIAR_CORRECTIVO           = "INICIAR_CORRECTIVO";
    public static final String MODIFICAR_CORRECTIVO         = "MODIFICAR_CORRECTIVO";
    public static final String FINALIZAR_CORRECTIVO         = "FINALIZAR_CORRECTIVO";
    public static final String GET_PREVIEW_CORRECTIVOS      = "GET_PREVIEW_CORRECTIVOS";
    public static final String CORREGIR_ACTIVO_FALLA        = "CORREGIR_ACTIVO_FALLA";


    public static void getCorrectivos(String tag, int userId, Callback<GetCorrectivosResult> callback) {
        new GetCorrectivosAction(userId).get(tag, callback);
    }

    public static void getCorrectivos(int userId, Callback<GetCorrectivosResult> callback) {
        getCorrectivos(GET_CORRECTIVOS, userId, callback);
    }

    public static void getActivosConFallas(Long idCorrectivo, Callback<GetActivosConFallasResult> callback) {
        new GetActivosConFallasAction(idCorrectivo).get(GET_ACTIVOS_FALLAS, callback);
    }

    public static void getActivosNoReportados(Long idEstacion, Long idCorrectivo, Callback<GetActivosNoReportadosResult> callback) {
        new GetActivosNoReportadosAction(idEstacion, idCorrectivo).get(GET_ACTIVOS_NO_REPORTADOS, callback);
    }

    public static void getSoluciones(Long idCorrectivo, Long idActivo, Callback<GetSolucionesResult> callback) {
        new GetSolucionesAction(idCorrectivo, idActivo).get(GET_SOLUCIONES, callback);
    }

    public static void getPendientes(Long idActivo, Callback<PendientesResult> callback) {
        new GetPendientesCorregidoAction(idActivo).get(GET_PENDIENTES_CORREGIDO, callback);
    }

    public static void savePendiente(SavePendienteCorrectivoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_PENDIENTE, callback);
    }

    public static void saveSolucion(SaveSolucionAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_SOLUCION, callback);
    }

    public static void deleteSolucion(Long idSolucion, Callback<BaseResult> callback) {
        new DeleteSolucionAction(idSolucion).get(DELETE_SOLUCION, callback);
    }

    public static void deletePendiente(Long idPendiente, Callback<BaseResult> callback) {
        new DeletePendienteCorregidoAction(idPendiente).get(DELETE_PENDIENTE, callback);
    }

    public static void iniciarCorrectivo(IniciarCorrectivoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(INICIAR_CORRECTIVO, callback);
    }

    public static void modificarCorrectivo(ModificarCorrectivoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(MODIFICAR_CORRECTIVO, callback);
    }

    public static void finalizarCorrectivo(FinalizarCorrectivoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(FINALIZAR_CORRECTIVO, callback);
    }

    public static void getPreview(Long idCorrectivo, Callback<GetPreviewCorrectivoResult> callback) {
        new GetPreviewCorrectivoAction(idCorrectivo).get(GET_PREVIEW_CORRECTIVOS, callback);
    }

    public static void corregirActivo(Long idCorrectivo, Long idActivoReportado, Long idActivoCorregido, Callback<BaseResult> callback) {
        new CorregirActivoDeFallaReportadaAction(idCorrectivo, idActivoReportado, idActivoCorregido)
                .get(CORREGIR_ACTIVO_FALLA, callback);
    }

}
