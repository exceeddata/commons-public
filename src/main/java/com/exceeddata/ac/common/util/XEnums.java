package com.exceeddata.ac.common.util;

import com.exceeddata.ac.common.data.record.EmptyIdent;
import com.exceeddata.ac.common.data.record.Ident;

public final class XEnums {
    private XEnums() {}
    
    public static final Ident EMPTY_KEY = EmptyIdent.INSTANCE;

    public static final String FILE_NAME        = "exd_file_name";
    public static final String FILE_PATH        = "exd_file_path";
    public static final String FILE_FOLDER      = "exd_file_folder";
    public static final String ZIP_SUBFILE      = "exd_zip_subfile";
    
    public static final String OUTPUT_ID        = "exd_output_id";
    public static final String PARTITION_ID     = "exd_partition_id";
    public static final String ROW_ID           = "exd_row_id";
    public static final String ROWS_PART_CNT    = "exd_rows_partition_cnt";
    public static final String ROWS             = "exd_rows";
    
    public static final String SESSION_ID       = "exd_session_id";
    public static final String SESSION_NUM      = "exd_session_num";
    public static final String SESSION_SIZE     = "exd_session_size";
    public static final String SESSION_ROWID    = "exd_session_rowid";
    public static final String WINDOW_ID        = "exd_window_id";
    public static final String WINDOW_NUM       = "exd_window_num";
    public static final String WINDOW_SIZE      = "exd_window_size";
    public static final String VIEW_ID          = "exd_view_id";
    public static final String VIEW_NUM         = "exd_view_num";
    public static final String VIEW_SIZE        = "exd_view_size";
    public static final String VIEW_ROWID    = "exd_view_rowid";
    public static final String STEP_NUM         = "exd_step_%05d";

    public static final String SIMPLE_SCHEMA         = "EXCEEDDATA_SIMPLE_RECORD_SCHEMA";
}
