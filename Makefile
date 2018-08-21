commit-site:
	mvn clean site:site
	git checkout gh-pages
	cp -R target/site/* ./
	git add .
	git commit -am "Add newest version of Maven site"
	git push
	git checkout master

release:
	mvn release:prepare
	mvn release:perform
	mvn release:clean
