package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import conection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOUsuarioRepository {
	
	private Connection connection;
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}

	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {
		
		if(objeto.isNovo()) { //grava um novo
		String sql="insert into model_login(login,senha,nome,email,usuario_id,perfil,sexo,cep, logradouro, bairro, localidade, uf, numero, datanascimento, rendamensal ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, objeto.getLogin());
		statement.setString(2, objeto.getSenha());
		statement.setString(3, objeto.getNome());
		statement.setString(4, objeto.getEmail());
		statement.setLong(5, userLogado);
		statement.setString(6, objeto.getPerfil());
		statement.setString(7, objeto.getSexo());
		statement.setString(8, objeto.getCep());
		statement.setString(9, objeto.getLogradouro());
		statement.setString(10, objeto.getBairro());
		statement.setString(11, objeto.getLocalidade());
		statement.setString(12, objeto.getUf());
		statement.setString(13, objeto.getNumero());
		statement.setDate(14, objeto.getDataNascimento());
		statement.setDouble(15, objeto.getRendamensal());
		statement.execute();
		connection.commit();
		if(objeto.getFotouser()!=null && !objeto.getFotouser().isEmpty()) {
			sql = "UPDATE model_login SET fotouser=?, extensaofotouser=? WHERE login=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, objeto.getFotouser());
			statement.setString(2, objeto.getExtensaofotouser());
			statement.setString(3, objeto.getLogin());
			statement.executeUpdate();
			connection.commit();
		}
		
		}else {
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, fotouser=?, extensaofotouser=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, datanascimento=?, rendamensal=?"
					+ " WHERE id="+objeto.getId();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());
			statement.setString(5, objeto.getPerfil());
			statement.setString(6, objeto.getSexo());
			statement.setString(7, objeto.getFotouser());
			statement.setString(8, objeto.getExtensaofotouser());
			statement.setString(9, objeto.getCep());
			statement.setString(10, objeto.getLogradouro());
			statement.setString(11, objeto.getBairro());
			statement.setString(12, objeto.getLocalidade());
			statement.setString(13, objeto.getUf());
			statement.setString(14, objeto.getNumero());
			statement.setDate(15, objeto.getDataNascimento());
			statement.setDouble(16, objeto.getRendamensal());
			
			statement.executeUpdate();
			connection.commit();
		}
		 return this.consultaUsuario(objeto.getLogin(), userLogado);
	}
	
	public int totalPagina(Long userLogado) throws Exception {
		String sql = "select count(1) as total from model_login where usuario_id="+userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultado = statement.executeQuery();
		resultado.next();
		Double cadastros  = resultado.getDouble("total");
		Double porPagina= 5.0;
		Double pagina = cadastros / porPagina;
		Double resto = pagina % 2;
		if(resto>0) {
			pagina++;
		}
		return pagina.intValue();
		} 
	
	
	public List<ModelLogin> consultaUsuarioListPagianado(Long userLogado, Integer offset) throws Exception{
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "select * from model_login where useradmin is false and usuario_id ="+userLogado +" order by nome offset ? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, offset);
		ResultSet resultado = statement.executeQuery();
		while(resultado.next()) {
			ModelLogin oModelLogin = new ModelLogin();
			oModelLogin.setId(resultado.getLong("id"));
			oModelLogin.setNome(resultado.getString("nome"));
			oModelLogin.setEmail(resultado.getString("email"));
			oModelLogin.setLogin(resultado.getString("login"));
			//oModelLogin.setSenha(resultado.getString("senha"));
			oModelLogin.setPerfil(resultado.getString("perfil"));
			oModelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(oModelLogin);
		}
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception{
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "select * from model_login where useradmin is false and usuario_id=? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, userLogado);
		ResultSet resultado = statement.executeQuery();
		while(resultado.next()) {
			ModelLogin oModelLogin = new ModelLogin();
			oModelLogin.setId(resultado.getLong("id"));
			oModelLogin.setNome(resultado.getString("nome"));
			oModelLogin.setEmail(resultado.getString("email"));
			oModelLogin.setLogin(resultado.getString("login"));
			//oModelLogin.setSenha(resultado.getString("senha"));
			oModelLogin.setPerfil(resultado.getString("perfil"));
			oModelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(oModelLogin);
		}
		return retorno;
	}
	
	
	
	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception{
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id=? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%"+nome+"%");
		statement.setLong(2, userLogado);
		ResultSet resultado = statement.executeQuery();
		while(resultado.next()) {
			ModelLogin oModelLogin = new ModelLogin();
			oModelLogin.setId(resultado.getLong("id"));
			oModelLogin.setNome(resultado.getString("nome"));
			oModelLogin.setEmail(resultado.getString("email"));
			oModelLogin.setLogin(resultado.getString("login"));
			//oModelLogin.setSenha(resultado.getString("senha"));
			oModelLogin.setPerfil(resultado.getString("perfil"));
			oModelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(oModelLogin);
		}
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioListOffSet(String nome, Long userLogado, int offset) throws Exception{
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id=? offset "+offset+" limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%"+nome+"%");
		statement.setLong(2, userLogado);
		ResultSet resultado = statement.executeQuery();
		while(resultado.next()) {
			ModelLogin oModelLogin = new ModelLogin();
			oModelLogin.setId(resultado.getLong("id"));
			oModelLogin.setNome(resultado.getString("nome"));
			oModelLogin.setEmail(resultado.getString("email"));
			oModelLogin.setLogin(resultado.getString("login"));
			//oModelLogin.setSenha(resultado.getString("senha"));
			oModelLogin.setPerfil(resultado.getString("perfil"));
			oModelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(oModelLogin);
		}
		return retorno;
	}
	
	public int consultaUsuarioListPaginaPaginacao(String nome, Long userLogado) throws Exception{
		String sql = "select count(1) as total from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id=? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%"+nome+"%");
		statement.setLong(2, userLogado);
		ResultSet resultado = statement.executeQuery();
		resultado.next();
		Double cadastros  = resultado.getDouble("total");
		Double porPagina= 5.0;
		Double pagina = cadastros / porPagina;
		Double resto = pagina % 2;
		if(resto>0) {
			pagina++;
		}
		return pagina.intValue();
	}
	
	public ModelLogin consultaUsuario(String login, Long userLogado ) throws Exception{
		ModelLogin modelLogin =  new ModelLogin();
		String sql = "select * from model_login where upper(login) = upper('"+login+"') and useradmin is false and usuario_id="+userLogado;
		PreparedStatement retorno = connection.prepareStatement(sql);
		ResultSet resultado = retorno.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

			
		}
		
		return modelLogin;
		} 
	
	public ModelLogin consultaUsuario(String login) throws Exception{
		ModelLogin modelLogin =  new ModelLogin();
		String sql = "select * from model_login where upper(login) = upper('"+login+"') and useradmin is false";
		PreparedStatement retorno = connection.prepareStatement(sql);
		ResultSet resultado = retorno.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		}
		
		return modelLogin;
		} 
	
	public ModelLogin consultaUsuarioLogado(String login) throws Exception{
		ModelLogin modelLogin =  new ModelLogin();
		String sql = "select * from model_login where upper(login) = upper('"+login+"')";
		PreparedStatement retorno = connection.prepareStatement(sql);
		ResultSet resultado = retorno.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));;
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
		} 
	
	public ModelLogin consultaUsuarioID(String id, Long userLogado) throws Exception{
		ModelLogin modelLogin =  new ModelLogin();
		String sql = "select * from model_login where id=? and useradmin is false and usuario_id=?";
		PreparedStatement retorno = connection.prepareStatement(sql);
		retorno.setLong(1, Long.parseLong(id));
		retorno.setLong(2, userLogado);
		ResultSet resultado = retorno.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
		} 
		
		public boolean validarLogin(String login)throws Exception{
			String sql="select count(1) > 0 as existe from model_login where upper(login) = upper('"+login+"')";
			PreparedStatement retorno = connection.prepareStatement(sql);
			ResultSet resultado = retorno.executeQuery();
			resultado.next(); //entra nos resultados do sql
			return resultado.getBoolean("existe");
		}
		
		public void deletarUser(String idUser) throws SQLException {
			String sql = "delete from model_login where id =? and useradmin is false";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, Long.parseLong(idUser));
			statement.executeUpdate();
			connection.commit();
		}
		
		public ModelLogin consultaUsuarioID(Long id) throws Exception{
			ModelLogin modelLogin =  new ModelLogin();
			String sql = "select * from model_login where id=? and useradmin is false";
			PreparedStatement retorno = connection.prepareStatement(sql);
			retorno.setLong(1, id);
			ResultSet resultado = retorno.executeQuery();
			while (resultado.next()) {
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setNome(resultado.getString("nome"));
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setLogin(resultado.getString("login"));
				//modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setSexo(resultado.getString("sexo"));
				modelLogin.setFotouser(resultado.getString("fotouser"));
				modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
				modelLogin.setCep(resultado.getString("cep"));
				modelLogin.setLogradouro(resultado.getString("logradouro"));
				modelLogin.setBairro(resultado.getString("bairro"));
				modelLogin.setLocalidade(resultado.getString("localidade"));
				modelLogin.setUf(resultado.getString("uf"));
				modelLogin.setNumero(resultado.getString("numero"));
				modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
				modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
			}
			
			return modelLogin;
			}
		public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception{
			List<ModelLogin> retorno = new ArrayList<ModelLogin>();
			String sql = "select * from model_login where useradmin is false and usuario_id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, userLogado);
			ResultSet resultado = statement.executeQuery();
			while(resultado.next()) {
				ModelLogin oModelLogin = new ModelLogin();
				oModelLogin.setId(resultado.getLong("id"));
				oModelLogin.setNome(resultado.getString("nome"));
				oModelLogin.setEmail(resultado.getString("email"));
				oModelLogin.setLogin(resultado.getString("login"));
				//oModelLogin.setSenha(resultado.getString("senha"));
				oModelLogin.setPerfil(resultado.getString("perfil"));
				oModelLogin.setSexo(resultado.getString("sexo"));
				oModelLogin.setTelefones(this.listFone(oModelLogin.getId()));

				retorno.add(oModelLogin);
			}
			return retorno;
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
				modelTelefone.setUsuario_pai_id(this.consultaUsuarioID(resultado.getLong("usuario_pai_id")));
				modelTelefone.setUsuario_cad_id(this.consultaUsuarioID(resultado.getLong("usuario_cad_id")));
				retorno.add(modelTelefone);
			}
			
			return retorno; 
			
		}
		
		public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dataInicial, String dataFinal) throws Exception{
			List<ModelLogin> retorno = new ArrayList<ModelLogin>();
			String sql = "select * from model_login where useradmin is false and usuario_id=? and datanascimento >=? and datanascimento <=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, userLogado);
			statement.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
			statement.setDate(3, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));
			ResultSet resultado = statement.executeQuery();
			while(resultado.next()) {
				ModelLogin oModelLogin = new ModelLogin();
				oModelLogin.setId(resultado.getLong("id"));
				oModelLogin.setNome(resultado.getString("nome"));
				oModelLogin.setEmail(resultado.getString("email"));
				oModelLogin.setLogin(resultado.getString("login"));
				//oModelLogin.setSenha(resultado.getString("senha"));
				oModelLogin.setPerfil(resultado.getString("perfil"));
				oModelLogin.setSexo(resultado.getString("sexo"));
				oModelLogin.setTelefones(this.listFone(oModelLogin.getId()));

				retorno.add(oModelLogin);
			}
			return retorno;
		}
		
	}
