<?php
// MAJ
function printStore2($bdd,$max)
{
	$labels = collectLabels($bdd,$max);
	$domains = collectDomains($bdd,$max);
	$appels = collectAppels($bdd,$max);
	$specs = collectSpecs($bdd,$max);
	$prices = collectPrices($bdd,$max);
	$qte = collectQtes($bdd,$max);
	echo '<ul>';
	for ($i=0 ; $i<$max ; $i++)
	{
		echo '<li>';
		echo '<img src="images/store/'.$labels[$i].'.jpeg" alt="" height="130px" classe="img-thumbnail"/></br>';
		echo '<i><font size="6">'.$domains[$i].'</font></i></br>';
		echo 'Appelation : <b>'.$appels[$i].'</b> ( '.$specs[$i].' ) </br>';
		echo 'Prix : '.$prices[$i].'€ Stock Restant : '.$qte[$i];
		echo '<select class="form-control" name="nb'.$labels[$i].'">
													  <option value="0">None</option>
													  <option value="1">1</option>
													  <option value="2">2</option>
													  <option value="3">3</option>
													  <option value="4">4</option>
													  <option value="5">5</option>
												</select>';
		echo '</li></br>';
	}
	echo '</ul>';
}

//MAJ
// Récupère les données du formulaire
function getCommand($bdd,$max)
{
	$labels = collectLabels($bdd,$max);
	for ($i=0 ; $i<$max ; $i++)
	{
		$nom = 'nb'.$labels[$i];
		$form[$i] = $_POST["$nom"];
	}
	return $form;
}

function printSelect($selectedItem,$sizeSelect)
{
	for ($i=0 ; $i<$sizeSelect ; $i++)
	{
		if ($i == $selectedItem)
			echo '<option value="'.$i.'" selected>'.$i.'</option>';
		else
			echo '<option value="'.$i.'">'.$i.'</option>';
	}
}

// MAJ
function dataBaseConnect() 
{
	try
    {
        $bdd = new PDO('mysql:host=localhost;dbname=oeno', "oeno", "Hahie=c1");
    }
    catch(Exception $e)
    {
            die('Erreur : '.$e->getMessage());
    }
    mysql_query("SET NAMES UTF8");
    return $bdd;
}

//MAJ
// Affiche les articles du caddie
function printCaddie($qte,$nom,$label,$prix)
{
		echo '<li type="disc">'.$qte.' X '.$nom.' ( '.$prix.' € ) : ';
		echo '<img src="images/store/'.$label.'.jpeg" width="30%" alt="" class="img-thumbnail"/></li>';
}

// MAJ
function printAdmin($qte,$nom)
{
		echo '['.$qte.' X '.$nom.']';
}

// MAJ
function getTotalPrice($entree,$price,$max)
{
	$bill = 0;
	for ($i = 0; $i < $max; $i++)
	{
		$bill = $bill + ($entree[$i] * $price[$i]);
	}
	return $bill;
}


// MAJ (passe du moins)
function checkDataEntrees($previousOrder,$entree,$stock,$max)
{
	// Si un des articles du client excède 5 qtés
	if (checkGreedy($previousOrder,$entree))
	{
	  echo "<center>Pour satisfaire tout le monde, nous limitons 5 articles par vin/pack. 
		  Il semble que vous ayez dépassé cette quantité (voir ci-dessous).</center>";
		  return 0;
	}
	else
	{
		for ($i=0 ; $i<$max ; $i++)
		{
			if (($entree[$i] < 0)||($entree[$i] > 5)||(!is_numeric($entree[$i])))
			{
				echo "Quantités entrées non reconnues";
				return 0;
			}
			else if ($entree[$i] > $stock[$i])
			{
				echo "Nous n'avons pas assez de stock par rapport à votre commande";
				return 0;
			}
		}
	}
	return 1;
}


//MAJ
function collectFormulaire($bdd,$login,$max)
{
	$query = $bdd->query('SELECT * FROM Formulaire') or die(print_r($bdd->errorInfo()));
	$labels = collectLabels($bdd,$max);
    $cmp = 0;

    while ($donnees2 = $query->fetch())
    {
        if($donnees2['login']==$login)
        {
        	for ($i=0 ; $i<$max ; $i++)
        	{
        		$res[$i] = $donnees2[$labels[$i]];
        	}
        	$cmp = 1;
        }
    }

    if ($cmp == 0)
    {
    	for ($i=0 ; $i<$max ; $i++)
        	{
        		$res[$i] = 0;
        	}
    }
    return $res;
}

// MAJ
function getBill($bdd,$login)
{
	$query = $bdd->query('SELECT * FROM Formulaire') or die(print_r($bdd->errorInfo()));
	while ($donnees = $query->fetch())
	{
		if($donnees['login']==$login)
		{
			return $donnees['prix'];
		}
	}
	return 0;
}

function paid($bdd,$login)
{
	$query = $bdd->query('SELECT * FROM Formulaire') or die(print_r($bdd->errorInfo()));
	while ($donnees = $query->fetch())
	{
		if($donnees['login']==$login)
		{
			return $donnees['paid'];
		}
	}
	return 0;
}

// MAJ
function didPaid($bdd,$login)
{
	return paid($bdd,$login) == getBill($bdd,$login);
}

// MAJ
function estVideCaddie($bdd,$login,$max)
{
	$basket = collectFormulaire($bdd,$login,$max);
	for ($i=0 ; $i<$max ; $i++)
	{
		if ($basket[$i] != 0)
			return 0;
	}
	return 1;
}

// MAJ
function updateStocks($bdd,$array,$max) 
{
	$stock = collectQtes($bdd,$max);
	$labels = collectLabels($bdd,$max);

	$con = mysql_connect("localhost", "oeno", "Hahie=c1");
    $db = mysql_select_db("oeno", $con);

        
	for ($i=0 ; $i<$max ; $i++)
	{
		$newValue = $stock[$i] - $array[$i];
		$req = ("UPDATE Stocks2 SET qte = '".$newValue."' WHERE label = '".$labels[$i]."'");
        $result = mysql_query($req,$con);
    	if (!$result) {
			die('Invalid query_update_stock: ' . mysql_error());
		}
	}
}

// MAJ
// Pour la création d'un client ou l'évolution d'un autre, on concatène les données sur les vins avec les infos sur le client
function updateFormulaire($bdd,$entree,$newValues,$login,$prix,$max) 
{
	$labels = collectLabels($bdd,$max);
	$detail_name = array("login","prix","paid");
	$labels = array_merge($labels,$detail_name);

	if(estVideCaddie($bdd,$login,$max)==1)
     {
    	$key = implode(", ",$labels);

     	$detail_value = array($login,$prix,'0');
     	$entree = array_merge($entree,$detail_value);
     	$value = "'".implode("','",$entree)."'";

     	$con = mysql_connect("localhost", "oeno", "Hahie=c1");
        $db = mysql_select_db("oeno", $con);

        $req = ("INSERT INTO Formulaire ($key) VALUES ($value)");
        $result = mysql_query($req,$con);
    	if (!$result) {
			die('Invalid query_new_customer: ' . mysql_error());
		}
    }
    else
    {
    	$paid = paid($bdd,$login);
      	$detail_value = array($login,$prix,$paid);
     	$newValues = array_merge($newValues,$detail_value);

     	$setting = fusionUpdate($labels,$newValues);
     	$setting = implode(",",$setting);

  		$con = mysql_connect("localhost", "oeno", "Hahie=c1");
    	$db = mysql_select_db("oeno", $con);

      	$query = ("UPDATE Formulaire SET $setting WHERE login = '".$login."'");
  		$result = mysql_query($query,$con);
		if (!$result) {
			die('Invalid query_update_customer: ' . mysql_error());
     	}
     }
}


// MAJ
function checkGreedy($array1,$array2)
{
	$limit = min(count($array1),count($array2));
	for ($i = 0; $i < $limit; $i++)
	{
		if ($array1[$i] + $array2[$i] > 5)
			return 1;
	}
	return 0;
}

// Affiche le descriptif de chaque région
function printRegion($winetext)
{
	echo "$winetext";
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//											DATA                                                         //
///////////////////////////////////////////////////////////////////////////////////////////////////////////

// Rassemble les données de tous les vins en vente

$price = array (25,10,9.5,6.5,7.5,15,11,6,7.5);
				
// Descriptif de différentes régions du vignoble bordelais
$medoctxt = 'La situation géographique privilégiée des vignes, combinée à un sous-sol unique
			et à des croupes graveleuses donne naissance à de grands vins.
			Le Cabernet-Sauvignon étant le cépage prédominant, les vins du Médoc,
			souvent destinés à la garde, sont dotés d\'une forte austérité.';

$gravestxt = 'Langue de vigne au Sud de Bordant pouvant atteindre, 20km d\'Ouest en Est.
			Cette amplitude explique l\'absence d\'unité du vignoble. On rappellera que 
			le nom conféré à cette région est du à la constitution de ses sols,
			une série d\'affleurements de dépôts sédimentaires.';

$sauternestxt = 'Originaire du Sud des Graves, il constitue l\'un des plus grands
				vins blancs liquoreux du monde. Vin sensuel aux couleurs d\'or et aux notes
				profondes de miel, de noisette et d\'orange.
				A boire très frais, il accompagne volontier une tranche de foie
				gras ou un dessert.';

$merstxt = 'Frontière entre la Gironde et la Dordogne, c\'est la zone viticole AOC la
			plus vaste de France. L\'Entre-deux-Mers est généralement un vin blanc sec 
			issu de Sauvignon et de Sémillon.';

$blayetxt = 'De style "rive droite", les vins de Blaye avaient la réputation d\'être
			robustes, sombre et de longue garde. Aujourd\'hui, ils sont souvent comparés
			aux Medocs au niveau de leur structure, leur fruité et leur caractère.';


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
														// 2nd Version //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


// C'est le nombre d'article qui est désigné par $max dans toute la bdd
function sizeCave()
{
	$con = mysql_connect("localhost", "oeno", "Hahie=c1");
	if (!$con)
	  {
	  die('Could not connect: ' . mysql_error());
	  }

	$db_selected = mysql_select_db("oeno",$con);

	$sql = "SELECT * FROM Stocks2";
	$result = mysql_query($sql,$con);

	mysql_close($con);
	return mysql_num_rows($result);
}

function collectLabels($bdd,$max)
{
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		$array[$i] = $donnees['label'];
	}
	return $array;
}

function collectDomains($bdd,$max)
{
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		$array[$i] = $donnees['domain'];
	}
	return $array;
}

function collectAppels($bdd,$max)
{
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		$array[$i] = $donnees['appel'];
	}
	return $array;
}

function collectSpecs($bdd,$max)
{
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		$array[$i] = $donnees['spec'];
	}
	return $array;
}

function collectPrices($bdd,$max)
{
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		$array[$i] = $donnees['prix'];
	}
	return $array;
}

function collectQtes($bdd,$max)
{
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		$array[$i] = $donnees['qte'];
	}
	return $array;
}

// From label
function getIndex($bdd,$max,$label)
{
	$index = -1;
	$query = $bdd->query('SELECT * FROM Stocks2') or die(print_r($bdd->errorInfo()));
	
	for ($i=0 ; $i<$max ; $i++)
	{
		$donnees = $query->fetch();
		if ($donnees['label'] == $label)
			$index = $i;
	}
	return $index;
}

function sumArray($array1,$array2)
{
	$lim = min(count($array1),count($array2));
	for ($i=0 ; $i<$lim ; $i++)
	{
		$res[$i] = $array1[$i] + $array2[$i];
	}
	return $res;
}

// Fonction annexe utilisée par la condition UPDATE de updateFormulaire
function fusionUpdate($array1,$array2)
{
	$lim = min(count($array1),count($array2));
	for ($i=0 ; $i<$lim ; $i++)
	{
		$str = $array1[$i]." = '".$array2[$i]."'";
		$res[$i] = $str;
	}
	return $res;
}

function array_zero($max)
{
	for ($i=0 ; $i<$max ; $i++)
	{
		$res[$i] = 0;
	}
	return $res;
}

function diffArray($array1,$array2)
{
	for ($i=0 ; $i<count($array1) ; $i++)
	{
		$res[$i] = $array1[$i] - $array2[$i];
	}
	return $res;
}

?>
