package com.medium.springbatch.multijob.model;

import com.medium.springbatch.multijob.model.CSV;
import com.medium.springbatch.multijob.util.CompositeIdentifier;
import com.medium.springbatch.multijob.util.MultiJobAppHelper;
import com.medium.springbatch.multijob.util.MultiJobAppSQLHelper;
import com.medium.springbatch.multijob.util.TableName;
import lombok.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("configuracao_busca")
@CompositeIdentifier("<<CONFIGURACAO_BUSCA>>")
public class UserCSV
		implements CSV {

    @Getter(AccessLevel.NONE)
    private String id;

    @Getter(AccessLevel.NONE)
    private String empresa_id;

    private String prioridade;
    private String nome;
    private String nac_int;
    private String versao;

    @Getter(AccessLevel.NONE)
    private String id_origem;
    private String status_configuracao;
    private String editavel;
    private String ativo;
    private String hash;
    private String status_publicacao;
    private String agrupa_chamada_unica;

    @Getter(AccessLevel.NONE)
    private String peso;

    public String getId() {
        return StringUtils.isEmpty(id) ? "0" : id;
    }

    public String getEmpresa_id() {
        return StringUtils.isEmpty(empresa_id) ? "0" : empresa_id;
    }

    public String getId_origem() {
        return StringUtils.isEmpty(id_origem) ? "0" : id_origem;
    }

    public String getPeso() {
        return StringUtils.isEmpty(peso) ? "0" : peso;
    }

    @Override
    public String sqlInsert() {
        return MultiJobAppSQLHelper.createInsertByFields(tableName(), MultiJobAppHelper.allFieldsFor(this.getClass()).map(Field::getName).toArray(String[]::new));
    }
}
