package scapp.apischuquiejdev.entity.solicitud;



public enum EstadoSolicitud {
    PENDIENTE,              // Generada por el agricultor.
    RECHAZADA,              // Rechazo inicial del beneficio.
    APROBADA,               // Aceptada para envío.
    EN_RECEPCION,           // Iniciaron los viajes.
    RECIBIDA,               // Fin de logística física (esperando liquidación).
    LIQUIDADA,              // Cerrada dentro del margen normal.
    LIQUIDADA_FALTANTE,     // Cerrada con pérdida/merma fuera de margen.
    LIQUIDADA_EXCEDENTE,    // Cerrada con extra fuera de margen.
    CANCELADA               // Anulada.
}