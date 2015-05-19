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

?>
	<?php include("head.php"); ?>
	<body class="right-sidebar">

		<!-- Header Wrapper -->
			<div id="header-wrapper">
						
				<!-- Header -->
					<div id="header" class="container">
						
						<!-- Logo -->
							<h1 id="logo"><a href="#">Store</a></h1>
							<p>Une sélection de vins à des prix préferentiels</p>
						
						<!-- Nav -->
						<?php include("nav.php"); ?>
							
					</div>

			</div>
			
		<!-- Main Wrapper -->
			<div id="main-wrapper">

				<!-- Main -->
					<div id="main" class="container">
						<div class="row">
								
							<!-- Content -->
								<div id="content" class="8u skel-cell-important">

									<!-- Post -->
										<article class="is-post">
											<header>
												<h2>La </strong> chasse aux <strong> grands crus </strong>est ouverte!<br />
												Laissez vous tenter par notre sélection</h2>
											</header>
											<span class="image image-full"><img src="images/caveavin.jpg" alt="" /></span>
											<p>
											<h3> Vous nous avez déjà fait commande? </h3>
											<a href="caddie.php?login=<?php echo $login; ?>" class="button button-icon fa fa-shopping-cart">Mon Caddie</a>
											</p>
											<br />
											<h3> Pour commencer ou compléter une commande </h3>
											<form method="post" action="caddie.php?login=<?php echo $login; ?>" enctype="multipart/form-data">

											<div class="form">

											<!-- Connection à la base de donnees -->
                                          	<?php
                                          		include("data-store.php");
                                          		$bdd = dataBaseConnect();
                                          		$max = sizeCave();
                                                printStore2($bdd,$max);
											?>
												<center>
												<input type="submit" class="button button-icon fa fa-shopping-cart" value="Valider" name="submit">
												</center>
											</div>
											</form>
											
											
										</article>
								
								</div>

							<!-- Sidebar -->
								<div id="sidebar" class="4u">
								
									<!-- Excerpts : Détail sur les régions -->
										<section>
											<ul class="divided">
												<li>

													<!-- Excerpt -->
														<article class="is-excerpt">
															<header>
																<h3>Medoc</h3>
															</header>
															<p><?php printRegion($medoctxt); ?>
															</p>
														</article>
												</li>

												<li>
													<!-- Excerpt -->
														<article class="is-excerpt">
															<header>
																<h3>Graves</h3>
															</header>
															<p><?php printRegion($gravestxt); ?></p>
														</article>

												</li>
												<li>

													<!-- Excerpt -->
														<article class="is-excerpt">
															<header>
																<h3>Sauternes</h3>
															</header>
															<p><?php printRegion($sauternestxt); ?></p>
														</article>

												</li>
												<li>

													<!-- Excerpt -->
														<article class="is-excerpt">
															<header>
																<h3>Entre-deux_mers</h3>
															</header>
															<p><?php printRegion($merstxt); ?></p>
														</article>

												</li>
												<li>

													<!-- Excerpt -->
														<article class="is-excerpt">
															<header>
																<h3>Blaye</h3>
															</header>
															<p><?php printRegion($blayetxt); ?></p>
														</article>

												</li>
											</ul>
										</section>
								</div>

						</div>
					</div>

			</div>

		<!-- Footer Wrapper -->
		<?php include("foot.php"); ?>

	</body>
</html>