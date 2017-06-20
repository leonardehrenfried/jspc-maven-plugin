commit-site:
	mvn clean site:site
	git checkout gh-pages
	cp -R target/site/* ./
	git commit -am "Add newest version of Maven site"
	git push
