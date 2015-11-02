/*jv.h*/

typedef enum {
  JV_KIND_INVALID,
  JV_KIND_NULL,
  JV_KIND_FALSE,
  JV_KIND_TRUE,
  JV_KIND_NUMBER,
  JV_KIND_STRING,
  JV_KIND_ARRAY,
  JV_KIND_OBJECT
} jv_kind;

typedef struct jv_refcnt {
  int count;
} jv_refcnt;


typedef struct {
  unsigned char kind_flags;
  unsigned char pad_;
  unsigned short offset;  /* array offsets */
  int size;
  union {
    struct jv_refcnt* ptr;
    double number;
  } u;
} jv;

char* jv_string_value(jv);
jv_kind jv_get_kind(jv);
jv jv_dump_string(jv, int flags);
void jv_free(jv);
jv jv_copy(jv);
jv jv_invalid_get_msg(jv);
int jv_invalid_has_msg(jv);

typedef struct jv_parser jv_parser;

jv_parser* jv_parser_new(int);
void jv_parser_free(jv_parser*);
void jv_parser_set_buf(jv_parser*, const char*, int, int);
jv jv_parser_next(jv_parser*);

/* jq.h */
typedef struct jq_state jq_state;
typedef void (*jq_msg_cb)(void *, jv);

jq_state *jq_init();
void jq_teardown(jq_state **);
void jq_set_error_cb(jq_state *, jq_msg_cb, void *);
int jq_compile(jq_state *, const char* str);
void jq_start(jq_state *, jv value, int flags);
jv jq_next(jq_state *);


