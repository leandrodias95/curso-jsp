package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conection.SingleConnectionBanco;
import model.ModelTelefone;

public class DAOTelefoneRepository {

	private Connection connection; 
	private DAOUsuarioRepository daoUsuarioRepository =  new DAOUsuarioRepository();
	
	public DAOTelefoneRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public void gravar(ModelTelefone modelTelefone) {
		
		String sql = "insert into telefone(numero, usuario_pai_id, usuario_cad_id ) values (?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, modelTelefone.getNumero());
			statement.setLong(2, modelTelefone.getUsuario_pai_id().getId());
			statement.setLong(3, modelTelefone.getUsuario_cad_id().getId());
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFone(Long id) throws SQLException {
		String sql = "delete from telefone where id = ? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
		statement.executeUpdate();
		connection.commit();
	}
	
	public List<ModelTelefone> listFone(Long idUserPai) throws Exception{
		List<ModelTelefone> retorno =  new ArrayList<ModelTelefone>();
		String sql = "select * from telefone where usuario_pai_id=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, idUserPai);
		ResultSet resultado = statement.executeQuery();
		while(resultado.next()) {
			ModelTelefone modelTelefone = new ModelTelefone();
			modelTelefone.setId(resultado.getLong("id"));
			modelTelefone.setNumero(resultado.getString("numero"));
			modelTelefone.setUsuario_pai_id(daoUsuarioRepository.consultaUsuarioID(resultado.getLong("usuario_pai_id")));
			modelTelefone.setUsuario_cad_id(daoUsuarioRepository.consultaUsuarioID(resultado.getLong("usuario_cad_id")));
			retorno.add(modelTelefone);
		}
		
		return retorno; 
		
	}
	
	public boolean existeFone(String fone, Long idUse) throws Exception  {
		String sql = "select count(1) >0 as existe from telefone where usuario_pai_id =? and numero= ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, idUse);
		statement.setString(2, fone);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getBoolean("existe");
	}
}
