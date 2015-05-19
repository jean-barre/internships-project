<?php
 
require_once 'CAS/CAS.php';
 
// Initialise phpCAS
phpCAS::client(CAS_VERSION_2_0,'cas.enseirb-matmeca.fr',443,'cas');
// Désactive la vérification SSL
phpCAS::setNoCasServerValidation();
// Force l'authentification CAS
phpCAS::forceAuthentication();
 
// getUser permet de récupérer le login de
// l'utilisateur connecté
$login = phpCAS::getUser();

$users = array();
	array_push($users,"jeabarre");
	array_push($users,"csauvage");
	array_push($users,"jduquenoy");
	array_push($users,"fljacquet");
	array_push($users,"fgarreau");
	array_push($users,"luraymon");
	array_push($users,"rboulch");
	array_push($users,"dagoncal");
	array_push($users,"elanternier");
	array_push($users,"mrobin001");
		
	if(!in_array($login, $users))
		header('Location: http://oeno.eirb.fr');

?>
<?php include("head.php"); ?>
	<body class="no-sidebar">

		<!-- Header Wrapper -->
			<div id="header-wrapper">
						
				<!-- Header -->
					<div id="header" class="container">
						
						<!-- Logo -->
							<h1 id="logo"><a href="admin2.php">Admin</a></h1>
							<p>Etat des Stocks</p>
						
						<!-- Nav -->
						<?php include("navadmin.php"); ?>

					</div>

			</div>
			
		<!-- Main Wrapper -->
			<div id="main-wrapper">

				<!-- Main -->
					<div id="main" class="container">
						<div class="row">
						
							<!-- Content -->
								<div id="content" class="12u skel-cell-important">

									<!-- Post -->
										<article class="is-post">
											<center>
												<p>
											<?php include("data-store.php");
											
											$bdd = dataBaseConnect();
											$max = sizeCave();

											// Pour ajouter un Vin
											if(isset($_POST['submit']))
											{
												if((isset($_POST['label']))&&isset($_POST['domain'])&&isset($_POST['appel'])
													&&isset($_POST['spec'])&&isset($_POST['prix'])&&isset($_POST['qte']))
	                                      		{
	                                      			$req = $bdd->prepare("INSERT INTO Stocks2(label,domain,appel,spec,prix,qte) 
	                                      				VALUES (:label, :domain, :appel, :spec, :prix, :qte)");
								                    $req->execute(array(
								                    'label' => $_POST['label'],
								                    'domain' => $_POST['domain'],
								                    'appel' => $_POST['appel'],
								                    'spec' => $_POST['spec'],
								                    'prix' => $_POST['prix'],
								                    'qte' => $_POST['qte']
								                	));

								                	$con = mysql_connect("localhost", "oeno", "Hahie=c1");
								                	$db = mysql_select_db("oeno", $con);
								                	$str = "ALTER TABLE Formulaire ADD ".$_POST['label']." INT(11) NOT NULL FIRST";
								                	$result = mysql_query($str,$con);
								                	if (!$result) {
    													die('Invalid query: ' . mysql_error());
													}
													mysql_close($con);

													echo "Ajout enregistré";
	                                      		}
	                                      		else
	                                      			echo "Ajout non enregistré, un champ n'a pas été saisi";
	                                      	}

	                                      	// Pour supprimer un vin
                                      		if(isset($_GET['delete']))
                                      		{
                                      			$label = $_GET['label'];
                                      			$con = mysql_connect("localhost", "oeno", "Hahie=c1");
								                $db = mysql_select_db("oeno", $con);

								                $req = "DELETE FROM Stocks2 WHERE label='".$label."'";
                                      			$result = mysql_query($req,$con);
							                	if (!$result) {
													die('Invalid query1: ' . mysql_error());
												}

												$str = "ALTER TABLE Formulaire DROP $label";
							                	$result2 = mysql_query($str,$con);
							                	if (!$result2) {
													die('Invalid query2: ' . mysql_error());
												}
												mysql_close($con);

												echo "Suppression enregistrée";
                                      		}

                                      		// Pour réapprovisionner un vin
                                      		if (isset($_POST['appro']))
                                      		{
                                      			$label = $_GET['label'];

                                      			// Création de la nouvelle valeur
                                      			$index = getIndex($bdd,$max,$label);
                                      			$oldValue = collectQtes($bdd,$max)[$index];
                                      			$newValue = $oldValue + $_POST['appro_value'];
                                      			
                                      			$con = mysql_connect("localhost", "oeno", "Hahie=c1");
								                $db = mysql_select_db("oeno", $con);

								                $req = ("UPDATE Stocks2 SET qte = '".$newValue."' WHERE label = '".$label."'");
                                      			$result = mysql_query($req,$con);
							                	if (!$result) {
													die('Invalid query1: ' . mysql_error());
												}
												mysql_close($con);

												echo "Réapprovisionnement enregistrée";
                                      		}


											$bdd = dataBaseConnect();
                                      		
											$max = sizeCave();
                                      		$labels = collectLabels($bdd,$max);
											$domains = collectDomains($bdd,$max);
											$appels = collectAppels($bdd,$max);
											$specs = collectSpecs($bdd,$max);
											$prices = collectPrices($bdd,$max);
											$qte = collectQtes($bdd,$max);

									
											for ($i=0 ; $i<count($labels) ; $i++)
											{
												echo '<li><b><font style="display: inline-block; width: 500px;" align="left" size="6">'.$qte[$i].' '.$domains[$i].'</font></b>';
												echo '<a href="stock.php?delete=1&label='.$labels[$i].'">Supprimer</a>';
												echo '<form style="display: inline-block; width: 300px;" method="post" action="stock.php?label='.$labels[$i].'" enctype="multipart/form-data">
														<div style="display: inline-block;">
														<label for="appro_id">Réapprovisionner :
															<select style="width: 5%;" name="appro_value" id="appro_id">
																<option value="1">1</option>
															    <option value="2">2</option>
															    <option value="5">5</option>
															    <option value="10">10</option>
															    <option value="20">20</option>
															    <option value="50">50</option>
															</select></label>
														</div>
															<input type="submit" class="btn btn-success" value="Add" name="appro">
													</form></li>';

											}
											echo '</ul>';
											?>
										</p>
											<a href="table.php" class="button button-icon fa fa-arrow-circle-down">Ajouter une Référence</a></br>
										</article>
								</div>
						</div>
					</div>
			</div>
	
	<?php include("foot.php"); ?>
	</body>
</html>