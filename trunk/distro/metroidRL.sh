if [ -f lib/libjcurses.dll ]; then
  rm lib/libjcurses.dll
fi

java -jar metroidrl.jar sc
