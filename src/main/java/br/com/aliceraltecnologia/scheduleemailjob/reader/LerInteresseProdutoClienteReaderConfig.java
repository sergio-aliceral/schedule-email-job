package br.com.aliceraltecnologia.scheduleemailjob.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import br.com.aliceraltecnologia.scheduleemailjob.dominio.Cliente;
import br.com.aliceraltecnologia.scheduleemailjob.dominio.InteresseProdutoCliente;
import br.com.aliceraltecnologia.scheduleemailjob.dominio.Produto;

@Configuration
public class LerInteresseProdutoClienteReaderConfig {
	@Bean
	public JdbcCursorItemReader<InteresseProdutoCliente> lerInteresseProdutoClienteReader(@Qualifier("appDataSource") DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<InteresseProdutoCliente>()
				.name("lerInteresseProdutoClienteReader")
				.dataSource(dataSource)
				.sql("SELECT * FROM interesse_produto_cliente " + 
					 "JOIN cliente ON (cliente = cliente.id) " + 
					 "JOIN produto ON (produto = produto.id)")
				.rowMapper(rowMapper())
				.build();
	}

	private RowMapper<InteresseProdutoCliente> rowMapper() {
		return new RowMapper<InteresseProdutoCliente>() {

			@Override
			public InteresseProdutoCliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				Cliente cliente = new Cliente();
				cliente.setId(rs.getInt("id"));
				cliente.setNome(rs.getString("nome"));
				cliente.setEmail(rs.getString("email"));

				Produto produto = new Produto();
				produto.setId(rs.getInt(6));
				produto.setNome(rs.getString(7));
				produto.setDescricao(rs.getString("descricao"));
				produto.setPreco(rs.getDouble("preco"));

				InteresseProdutoCliente interesseProdutoCliente = new InteresseProdutoCliente();
				interesseProdutoCliente.setCliente(cliente);
				interesseProdutoCliente.setProduto(produto);
				return interesseProdutoCliente;
			}
		};
	}

}
