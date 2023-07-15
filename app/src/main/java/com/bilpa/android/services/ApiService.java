package com.bilpa.android.services;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.CorregidosResult;
import com.bilpa.android.services.actions.DestinoCargoResult;
import com.bilpa.android.services.actions.FallasResult;
import com.bilpa.android.services.actions.GetActivoIdByQrAction;
import com.bilpa.android.services.actions.GetActivoIdByQrResult;
import com.bilpa.android.services.actions.GetChequeoBombaAction;
import com.bilpa.android.services.actions.GetChequeoBombaResult;
import com.bilpa.android.services.actions.GetChequeoGenericoAction;
import com.bilpa.android.services.actions.GetChequeoGenericoResult;
import com.bilpa.android.services.actions.GetChequeoSurtidorAction;
import com.bilpa.android.services.actions.GetChequeoSurtidorPicoAction;
import com.bilpa.android.services.actions.GetChequeoSurtidorPicoResult;
import com.bilpa.android.services.actions.GetChequeoSurtidorResult;
import com.bilpa.android.services.actions.GetChequeoTanqueAction;
import com.bilpa.android.services.actions.GetChequeoTanqueResult;
import com.bilpa.android.services.actions.GetCorregidosAction;
import com.bilpa.android.services.actions.GetDestinoCargosAction;
import com.bilpa.android.services.actions.GetFallasAction;
import com.bilpa.android.services.actions.GetOrganizacionesAction;
import com.bilpa.android.services.actions.GetPicosAction;
import com.bilpa.android.services.actions.GetRepuestosAction;
import com.bilpa.android.services.actions.GetTareasAction;
import com.bilpa.android.services.actions.GetTipoDescargaAction;
import com.bilpa.android.services.actions.GetTipoFallasAction;
import com.bilpa.android.services.actions.GetTipoTareasAction;
import com.bilpa.android.services.actions.GetTiposRepuestosAction;
import com.bilpa.android.services.actions.GetVisitaAction;
import com.bilpa.android.services.actions.GetVisitaResult;
import com.bilpa.android.services.actions.GetVisitasAsignadasAction;
import com.bilpa.android.services.actions.GetVisitasResult;
import com.bilpa.android.services.actions.LoginAction;
import com.bilpa.android.services.actions.LoginResult;
import com.bilpa.android.services.actions.OrganizacionesResult;
import com.bilpa.android.services.actions.PicosResult;
import com.bilpa.android.services.actions.RepuestosResult;
import com.bilpa.android.services.actions.SaveChequeoBombaAction;
import com.bilpa.android.services.actions.SaveChequeoGenericoAction;
import com.bilpa.android.services.actions.SaveChequeoSurtidorAction;
import com.bilpa.android.services.actions.SaveChequeoSurtidorPicoAction;
import com.bilpa.android.services.actions.SaveChequeoTanqueAction;
import com.bilpa.android.services.actions.SaveCorregidoAction;
import com.bilpa.android.services.actions.SaveCorregidoResult;
import com.bilpa.android.services.actions.SetQrToActivoAction;
import com.bilpa.android.services.actions.TareasResult;
import com.bilpa.android.services.actions.TipoDescargaResult;
import com.bilpa.android.services.actions.TiposFallaResult;
import com.bilpa.android.services.actions.TiposRepuestosResult;
import com.bilpa.android.services.actions.TiposTareaResult;
import com.bilpa.android.services.actions.activos.GetPicoIdByQrAction;
import com.bilpa.android.services.actions.activos.GetPicoIdByQrResult;
import com.bilpa.android.services.actions.activos.SetQrToPicoAction;
import com.bilpa.android.services.actions.comments.CommentsResult;
import com.bilpa.android.services.actions.comments.DeleteCheckCommentAction;
import com.bilpa.android.services.actions.comments.GetCommentsAction;
import com.bilpa.android.services.actions.comments.SaveCheckCommentAction;
import com.bilpa.android.services.actions.comments.UpdateCheckCommentAction;
import com.bilpa.android.services.actions.corregidos.DeleteCorregidoAction;
import com.bilpa.android.services.actions.pendientes.DeletePendienteAction;
import com.bilpa.android.services.actions.pendientes.DescartarPendienteAction;
import com.bilpa.android.services.actions.pendientes.GetPendientesAction;
import com.bilpa.android.services.actions.pendientes.PendientesResult;
import com.bilpa.android.services.actions.pendientes.SaveChequeoProductoAction;
import com.bilpa.android.services.actions.pendientes.SavePendienteAction;
import com.bilpa.android.services.actions.products.GetChequeoProductoAction;
import com.bilpa.android.services.actions.products.GetChequeoProductoResult;
import com.bilpa.android.services.actions.products.GetProductosAction;
import com.bilpa.android.services.actions.products.ProductosResult;
import com.bilpa.android.services.actions.visita.FinalizarVisitaAction;
import com.bilpa.android.services.actions.visita.GetCorreosEstacionAction;
import com.bilpa.android.services.actions.visita.GetCorreosEstacionResult;
import com.bilpa.android.services.actions.visita.GetReportAction;
import com.bilpa.android.services.actions.visita.ReportResult;
import com.bilpa.android.services.actions.visita.UpdateVisitaAction;

public class ApiService {

    public static final String LOGIN = "LOGIN";

    public static final String GET_VISITAS_ASIGNADAS = "GET_VISITAS_ASIGNADAS";

    public static final String GET_VISITA           = "GET_VISITA";
    public static final String SAVE_VISITA          = "SAVE_VISITA";
    public static final String FINISH_VISITA          = "FINISH_VISITA";
    public static final String GET_ACTIVO_BY_QR     = "GET_ACTIVO_BY_QR";
    public static final String TAG_SET_QR_ACTIVO     = "TAG_SET_QR_ACTIVO";

    public static final String GET_CHEQUEO          = "GET_CHEQUEO";
    public static final String SAVE_CHEQUEO         = "SAVE_CHEQUEO";
    public static final String GET_TIPOS_DESCARGAS  = "GET_TIPOS_DESCARGAS";
    public static final String SAVE_CHEQUEO_PICO    = "SAVE_CHEQUEO_PICO";


    public static final String GET_REPUESTOS = "GET_REPUESTOS";
    public static final String GET_TIPOS_REPUESTOS = "GET_TIPOS_REPUESTOS";
    public static final String GET_TAREAS = "GET_TAREAS";
    public static final String GET_TIPOS_TAREAS = "GET_TIPOS_TAREAS";
    public static final String GET_TIPOS_FALLAS = "GET_TIPO_FALLAS";
    public static final String GET_FALLAS = "GET_FALLAS";

    public static final String SAVE_CORREGIDO= "SAVE_CORREGIDO";
    public static final String GET_CORREGIDOS = "GET_CORREGIDOS";
    public static final String DELETE_CORREGIDO = "DELETE_CORREGIDO";

    public static final String GET_PICOS = "GET_PICOS";
    public static final String GET_DESTINO_CARGOS = "GET_DESTINO_CARGOS";
    public static final String GET_ORGANIZACIONES = "GET_ORGANIZACIONES";

    public static final String GET_PENDIENTES = "GET_PENDIENTES";
    public static final String SAVE_PENDIENTE = "SAVE_PENDIENTE";
    public static final String DELETE_PENDIENTE = "DELETE_PENDIENTE";
    public static final String DESCARTAR_PENDIENTE = "DESCARTAR_PENDIENTE";

    public static final String GET_PRODUCTOS = "GET_PRODUCTOS";
    public static final String GET_PICO_BY_QR = "GET_PICO_BY_QR";
    public static final String SET_QR_PICO = "SET_QR_PICO";

    public static final String GET_REPORT           = "GET_REPORT";
    public static final String GET_EMAILS_ESTACION  = "GET_EMAILS_ESTACION";

    public static final String GET_CHEQUEO_COMMENTS = "GET_CHEQUEO_COMMENTS";
    private static final String SAVE_CHECK_COMMENT = "SAVE_CHECK_COMMENT";
    private static final String UPDATE_CHECK_COMMENT = "UPDATE_CHECK_COMMENT";
    public static final String DELETE_CHECK_COMMENT = "DELETE_CHECK_COMMENT";

    public static void login(String user, String pass, Callback<LoginResult> callback) {
        new LoginAction(user, pass).get(LOGIN, callback);
    }

    public static void getVisitasAsignadas(String tag, int userId, Callback<GetVisitasResult> callback) {
        new GetVisitasAsignadasAction(userId).get(tag, callback);
    }

    public static void getVisitas(String tag, long visitaId, Callback<GetVisitaResult> callback) {
        new GetVisitaAction(visitaId).get(tag, callback);
    }

    public static void updateVisita(UpdateVisitaAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_VISITA, callback);
    }

    public static void finishVisita(FinalizarVisitaAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(FINISH_VISITA, callback);
    }

    public static void getEmailsEstacion(Long idEstacion, Callback<GetCorreosEstacionResult> callback) {
        new GetCorreosEstacionAction(idEstacion).get(GET_EMAILS_ESTACION, callback);
    }

    public static void getActivoIdByQr(String qrCode, Long idEstacion, Callback<GetActivoIdByQrResult> callback) {
        new GetActivoIdByQrAction(qrCode, idEstacion).get(GET_ACTIVO_BY_QR, callback);
    }

    public static void setQrToActivo(Long idActivo, String qrCode, Callback<BaseResult> callback) {
        new SetQrToActivoAction(idActivo, qrCode).get(TAG_SET_QR_ACTIVO, callback);
    }

    public static void getPicoIdByQr(String qrCode, Long idEstacion, Callback<GetPicoIdByQrResult> callback) {
        new GetPicoIdByQrAction(qrCode, idEstacion).get(GET_PICO_BY_QR, callback);
    }

    public static void setQrToPico(Long idPico, String qrCode, Callback<BaseResult> callback) {
        new SetQrToPicoAction(idPico, qrCode).get(SET_QR_PICO, callback);
    }

    public static void getChequeoSurtidor(Long idVisita, Long idActivo, Callback<GetChequeoSurtidorResult> callback) {
        new GetChequeoSurtidorAction(idVisita, idActivo).get(GET_CHEQUEO, callback);
    }

    public static void getChequeoSurtidorPico(Long idPico, Long idPreventivo, Callback<GetChequeoSurtidorPicoResult> callback) {
        new GetChequeoSurtidorPicoAction(idPico, idPreventivo).get(GET_CHEQUEO, callback);
    }

    public static void getChequeoTanque(Long idVisita, Long idActivo, Callback<GetChequeoTanqueResult> callback) {
        new GetChequeoTanqueAction(idVisita, idActivo).get(GET_CHEQUEO, callback);
    }

    public static void getChequeoBomba(Long idVisita, Long idActivo, Callback<GetChequeoBombaResult> callback) {
        new GetChequeoBombaAction(idVisita, idActivo).get(GET_CHEQUEO, callback);
    }

    public static void getChequeoGenerico(Long idVisita, Long idActivo, Callback<GetChequeoGenericoResult> callback) {
        new GetChequeoGenericoAction(idVisita, idActivo).get(GET_CHEQUEO, callback);
    }

    public static void getTiposDescargas(Callback<TipoDescargaResult> callback) {
        new GetTipoDescargaAction().get(GET_TIPOS_DESCARGAS, callback);
    }

    public static void saveChequeoSurtidor(SaveChequeoSurtidorAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHEQUEO, callback);
    }

    public static void saveChequeoSurtidorPico(SaveChequeoSurtidorPicoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHEQUEO_PICO, callback);
    }

    public static void saveChequeoTanque(SaveChequeoTanqueAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHEQUEO, callback);
    }

    public static void saveChequeoBomba(SaveChequeoBombaAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHEQUEO, callback);
    }

    public static void saveChequeoGenerico(SaveChequeoGenericoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHEQUEO, callback);
    }

    public static void getReport(Long idVisita, Callback<ReportResult> callback) {
        new GetReportAction(idVisita).get(GET_REPORT, callback);
    }


    /**
     * Productos
     */
    public static void getProductos(Long idSurtidor, Callback<ProductosResult> callback) {
        new GetProductosAction(idSurtidor).get(GET_PRODUCTOS, callback);
    }

    public static void getChequeoProducto(Long idProducto, Long idPreventivo, Callback<GetChequeoProductoResult> callback) {
        new GetChequeoProductoAction(idProducto, idPreventivo).get(GET_CHEQUEO, callback);
    }

    public static void saveChequeoProducto(SaveChequeoProductoAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHEQUEO, callback);
    }

    /**
     * Repuestos
     */

    public static void getRepuestos(String tag, Callback<RepuestosResult> callback) {
        new GetRepuestosAction().get(tag, callback);
    }
    public static void getTiposRepuestos(String tag, Callback<TiposRepuestosResult> callback) {
        new GetTiposRepuestosAction().get(tag, callback);
    }

    /**
     * Tareas
     */

    public static void getTareas(String tag, Callback<TareasResult> callback) {
        new GetTareasAction().get(tag, callback);
    }
    public static void getTareas(String tag, long type, Callback<TareasResult> callback) {
        new GetTareasAction(type).get(tag, callback);
    }
    public static void getTipoTareas(String tag, Callback<TiposTareaResult> callback) {
        new GetTipoTareasAction().get(tag, callback);
    }


    /**
     * Fallas
     */

    public static void getTipoFallas(String tag, Callback<TiposFallaResult> callback) {
        new GetTipoFallasAction().get(tag, callback);
    }

    public static void getTipoFallas(Callback<TiposFallaResult> callback) {
        new GetTipoFallasAction().get(GET_TIPOS_FALLAS, callback);
    }
    public static void getFallas(String tag, Callback<FallasResult> callback) {
        new GetFallasAction().get(tag, callback);
    }
    public static void getFallas(Callback<FallasResult> callback) {
        new GetFallasAction().get(GET_FALLAS, callback);
    }
    public static void getFallasByType(String tag, long type, Callback<FallasResult> callback) {
        new GetFallasAction(type).get(tag, callback);
    }
    public static void getFallasByType(long type, Callback<FallasResult> callback) {
        new GetFallasAction(type).get(GET_FALLAS, callback);
    }


    /**
     * Corregidos
     */
    public static void getCorregidos(Long idPreventivo, Callback<CorregidosResult> callback) {
        new GetCorregidosAction(idPreventivo).get(GET_CORREGIDOS, callback);
    }

    public static void getPicos(long idSurtidor, Callback<PicosResult> callback) {
        new GetPicosAction(idSurtidor).get(GET_PICOS, callback);
    }

    public static void getDestinoCargo(Callback<DestinoCargoResult> callback) {
        new GetDestinoCargosAction().get(GET_DESTINO_CARGOS, callback);
    }

    public static void getOrganizaciones(Callback<OrganizacionesResult> callback) {
        new GetOrganizacionesAction().get(GET_ORGANIZACIONES, callback);
    }


    public static void saveCorredigo(SaveCorregidoAction.Builder b, Callback<SaveCorregidoResult> callback) {
        b.create().post(SAVE_CORREGIDO, callback);
    }

    public static void deleteCorredigo(Long idCorregido, Callback<BaseResult> callback) {
        new DeleteCorregidoAction(idCorregido).get(DELETE_CORREGIDO, callback);
    }


    /**
     * Pendientes
     */
    public static void getPendientes(Long idPreventivo, Callback<PendientesResult> callback) {
        new GetPendientesAction(idPreventivo).get(GET_PENDIENTES, callback);
    }

    public static void savePendiente(SavePendienteAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_PENDIENTE, callback);
    }

    public static void deletePendiente(Long idPendiente, Callback<BaseResult> callback) {
        new DeletePendienteAction(idPendiente).get(DELETE_PENDIENTE, callback);
    }

    public static void descartarPendiente(Long idPendiente, Long idDescartador, String motivoDescarte, Callback<BaseResult> callback) {
        new DescartarPendienteAction(idPendiente, idDescartador, motivoDescarte).get(DESCARTAR_PENDIENTE, callback);
    }

    /**
     * Comentarios
     */
    public static void getCheckComments(Long chequeoId, Callback<CommentsResult> callback) {
        new GetCommentsAction(chequeoId).get(GET_CHEQUEO_COMMENTS, callback);
    }

    public static void saveCheckComment(SaveCheckCommentAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(SAVE_CHECK_COMMENT, callback);
    }

    public static void updateCheckComment(UpdateCheckCommentAction.Builder b, Callback<BaseResult> callback) {
        b.create().post(UPDATE_CHECK_COMMENT, callback);
    }

    public static void deleteComentario(Long idComentario, Callback<BaseResult> callback) {
        DeleteCheckCommentAction.Builder builder = new DeleteCheckCommentAction.Builder();
        builder.id = idComentario;
        builder.create().post(DELETE_CHECK_COMMENT, callback);
    }

}
